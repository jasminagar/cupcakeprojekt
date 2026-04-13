package controllers;

import entities.*;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.BottomService;
import services.OrderService;
import services.ToppingService;
import services.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderController {

    public static void addRoutes(Javalin app,
                                 OrderService orderService,
                                 UserService userService,
                                 BottomService bottomService,
                                 ToppingService toppingService) {

        app.get("/order", ctx -> showOrderPage(ctx, userService, bottomService, toppingService));

        app.post("/cart/add", ctx -> addToCart(ctx, bottomService, toppingService));

        app.post("/cart/remove/{index}", ctx -> removeFromCart(ctx));

        app.post("/checkout", ctx -> checkout(ctx, orderService));
    }

    private static void showOrderPage(Context ctx,
                                      UserService userService,
                                      BottomService bottomService,
                                      ToppingService toppingService) {

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
        if (cart == null) {
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
    }

    private static void addToCart(Context ctx,
                                  BottomService bottomService,
                                  ToppingService toppingService) {

        User sessionUser = ctx.sessionAttribute("currentUser");

        if (sessionUser == null) {
            ctx.redirect("/login");
            return;
        }

        int bottomId = Integer.parseInt(ctx.formParam("bottomId"));
        int toppingId = Integer.parseInt(ctx.formParam("toppingId"));
        int quantity = Integer.parseInt(ctx.formParam("quantity"));

        CupcakeBottom bottom = bottomService.getBottomById(bottomId);
        CupcakeTop topping = toppingService.getTopById(toppingId);

        double totalPrice = (bottom.getPrice() + topping.getPrice()) * quantity;

        CartItem item = new CartItem();
        item.setBottomId(bottomId);
        item.setToppingId(toppingId);
        item.setBottomName(bottom.getFlavour());
        item.setToppingName(topping.getFlavour());
        item.setQuantity(quantity);
        item.setTotalPrice(totalPrice);

        List<CartItem> cart = ctx.sessionAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
        }

        cart.add(item);
        ctx.sessionAttribute("cart", cart);

        ctx.redirect("/order");
    }

    private static void removeFromCart(Context ctx) {

        List<CartItem> cart = ctx.sessionAttribute("cart");

        if (cart != null) {
            int index = Integer.parseInt(ctx.pathParam("index"));

            if (index >= 0 && index < cart.size()) {
                cart.remove(index);
            }

            ctx.sessionAttribute("cart", cart);
        }

        ctx.redirect("/order");
    }

    private static void checkout(Context ctx, OrderService orderService) {

        User sessionUser = ctx.sessionAttribute("currentUser");

        if (sessionUser == null) {
            ctx.redirect("/login");
            return;
        }

        List<CartItem> cart = ctx.sessionAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            ctx.redirect("/order");
            return;
        }

        for (CartItem item : cart) {
            OrderRequest request = new OrderRequest();
            request.setBottomId(item.getBottomId());
            request.setToppingId(item.getToppingId());
            request.setQuantity(item.getQuantity());
            request.setUserId(sessionUser.getId());
            request.setPickupTime(LocalDateTime.now().plusDays(1));

            orderService.createOrder(request);
        }

        ctx.sessionAttribute("cart", new ArrayList<CartItem>());
        ctx.redirect("/order");
    }
}