package controllers;

import entities.OrderRequest;
import entities.OrderResponse;
import io.javalin.Javalin;
import services.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class OrderController {
    public OrderController(Javalin app, OrderService orderService) {
        app.post("/orders", ctx -> {
            try {
                int bottomId = Integer.parseInt(ctx.formParam("bottomId"));
                int toppingId = Integer.parseInt(ctx.formParam("toppingId"));
                int quantity = Integer.parseInt(ctx.formParam("quantity"));
                String pickupTimeStr = ctx.formParam("pickupTime");
                int userId = Integer.parseInt(ctx.formParam("userId"));

                LocalDateTime pickupTime = LocalDateTime.parse(pickupTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                // Lav OrderRequest
                OrderRequest request = new OrderRequest();
                request.setBottomId(bottomId);
                request.setToppingId(toppingId);
                request.setQuantity(quantity);
                request.setPickupTime(pickupTime);
                request.setUserId(userId);

                // Opret ordre via service
                OrderResponse response = orderService.createOrder(request);

                if(response == null){
                    ctx.status(400).result("Kunne ikke oprette ordren");
                } else {
                    ctx.redirect("/order-confirmation"); // evt. en side, der viser ordren
                }

            } catch (NumberFormatException e) {
                ctx.status(400).result("Ugyldige tal i formularen");
            } catch (DateTimeParseException e) {
                ctx.status(400).result("Ugyldig afhentningstid");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Der skete en fejl ved oprettelse af ordren");
            }
        });
    }
}
