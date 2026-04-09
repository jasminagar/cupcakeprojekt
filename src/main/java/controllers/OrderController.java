package controllers;

import entities.*;
import io.javalin.Javalin;
import services.BottomService;
import services.OrderService;
import services.ToppingService;
import services.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public OrderController(Javalin app,
                           OrderService orderService,
                           UserService userService,
                           BottomService bottomService,
                           ToppingService toppingService) {

        app.get("/order", ctx -> {
            User sessionUser = ctx.sessionAttribute("currentUser");

            if (sessionUser == null) {
                ctx.redirect("/login");
                return;
            }

            User currentUser = userService.getUserById(sessionUser.getId());

            if (currentUser == null) {
                ctx.redirect("/login");
                return;
            }

            List<CupcakeBottom> bottoms = bottomService.getAllBottoms();
            List<CupcakeTop> toppings = toppingService.getAllTops();

            List<CartItem> cart = ctx.sessionAttribute("cart");
            if (cart == null){
                cart = new ArrayList<>();
                ctx.sessionAttribute("cart", cart);
            }

            double totalCartPrice = cart.stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();

            ctx.attribute("bottoms", bottoms);
            ctx.attribute("toppings", toppings);
            ctx.attribute("orderRequest", new OrderRequest());
            ctx.attribute("currentUser", currentUser);
            ctx.attribute("balance", currentUser.getBalance());

            ctx.attribute("cart", cart);
            ctx.attribute("totalCartPrice", totalCartPrice);

            ctx.render("order.html");
        });

        app.post("/cart/remove/{index}", context -> {
           List<CartItem> cart = context.sessionAttribute("cart");

           if (cart != null){
               int index = Integer.parseInt(context.pathParam("index"));

               if (index >= 0 && index < cart.size()){
                   cart.remove(index);
               }

               context.sessionAttribute("cart", cart);
           }

           context.redirect("/order");
        });

        app.post("/checkout", context -> {
            User sessionUser = context.sessionAttribute("currentUser");

            if (sessionUser == null) {
                context.redirect("/login");
                return;
            }

            List<CartItem> cart = context.sessionAttribute("cart");

            if (cart == null || cart.isEmpty()) {
                context.redirect("/order");
                return;
            }

            for (CartItem item : cart){
                OrderRequest request = new OrderRequest();
                request.setBottomId(item.getBottomId());
                request.setToppingId(item.getToppingId());
                request.setQuantity(item.getQuantity());
                request.setUserId(sessionUser.getId());
                request.setPickupTime(LocalDateTime.now().plusDays(1));

                orderService.createOrder(request);
            }

            context.sessionAttribute("cart", new ArrayList<CartItem>());
            context.redirect("/order");
        });

        app.post("/cart/add", context -> {
           User sessionUser = context.sessionAttribute("currentUser");

           if (sessionUser == null){
               context.redirect("/login");
           }

           int bottomId = Integer.parseInt(context.formParam("bottomId"));
           int topId = Integer.parseInt(context.formParam("toppingId"));
           int quantity = Integer.parseInt(context.formParam("quantity"));

           CupcakeBottom cupcakeBottom = bottomService.getBottomById(bottomId);
           CupcakeTop cupcakeTop = toppingService.getTopById(topId);

            double totalPrice = (cupcakeBottom.getPrice() + cupcakeTop.getPrice()) * quantity;

            CartItem item = new CartItem();
            item.setBottomId(bottomId);
            item.setToppingId(topId);
            item.setBottomName(cupcakeBottom.getFlavour());
            item.setToppingName(cupcakeTop.getFlavour());
            item.setQuantity(quantity);
            item.setTotalPrice(totalPrice);

            List<CartItem> cart = context.sessionAttribute("cart");

            if (cart == null){
                cart = new ArrayList<>();
            }

            cart.add(item);

            context.sessionAttribute("cart", cart);

            context.redirect("/order");
        });

        /*app.post("/orders", ctx -> {
            try {
                User sessionUser = ctx.sessionAttribute("currentUser");

                if (sessionUser == null) {
                    ctx.redirect("/login");
                    return;
                }

                int bottomId = Integer.parseInt(ctx.formParam("bottomId"));
                int toppingId = Integer.parseInt(ctx.formParam("toppingId"));
                int quantity = Integer.parseInt(ctx.formParam("quantity"));
                String pickupTimeStr = ctx.formParam("pickupTime");
                int userId = Integer.parseInt(ctx.formParam("userId"));

                LocalDateTime pickupTime = LocalDateTime.parse(pickupTimeStr);

                OrderRequest request = new OrderRequest();
                request.setBottomId(bottomId);
                request.setToppingId(toppingId);
                request.setQuantity(quantity);
                request.setPickupTime(pickupTime);
                request.setUserId(userId);

                OrderResponse response = orderService.createOrder(request);

                if (response == null) {
                    ctx.status(400).result("Kunne ikke oprette ordren");
                } else {
                    ctx.redirect("/order");
                }

            } catch (NumberFormatException e) {
                ctx.status(400).result("Ugyldige tal i formularen");
            } catch (DateTimeParseException e) {
                ctx.status(400).result("Ugyldig afhentningstid");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Der skete en fejl ved oprettelse af ordren");
            }
        });*/
    }
}