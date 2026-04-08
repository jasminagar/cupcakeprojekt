package controllers;

import entities.*;
import io.javalin.Javalin;
import services.BottomService;
import services.OrderService;
import services.ToppingService;
import services.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {
    public OrderController(Javalin app, OrderService orderService, UserService userService,BottomService bottomService, ToppingService toppingService) {

        app.get("/order", ctx -> {
            List<CupcakeBottom> bottoms = bottomService.getAllBottoms();
            List<CupcakeTop> toppings = toppingService.getAllTops();
            User sessionUser = ctx.sessionAttribute("currentUser");
            User currentUser = userService.getUserById(sessionUser.getId());

            ctx.attribute("bottoms", bottoms);  // send til Thymeleaf
            ctx.attribute("toppings", toppings); // send til Thymeleaf
            ctx.attribute("orderRequest", new OrderRequest()); // til th:object
            ctx.attribute("currentUser", currentUser);
            ctx.attribute("balance", currentUser.getBalance());

            ctx.attribute("currentUser", currentUser); // hidden field

            ctx.render("order");
        });

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
                    ctx.redirect("/order-confirmation"); //LAV SIDE TIL DET
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
