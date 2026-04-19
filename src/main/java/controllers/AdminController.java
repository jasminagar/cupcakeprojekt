package controllers;

import entities.Order;
import entities.OrderLine;
import entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.OrderService;
import services.UserService;

import java.util.List;

public class AdminController {

    public static void addRoutes(Javalin app,
                                 UserService userService,
                                 OrderService orderService) {

        app.get("/admin", ctx -> showAdminPage(ctx, orderService, userService));

        app.post("/admin/addBalance", ctx -> addBalance(ctx, userService));

        app.post(("/admin/deleteOrder"), context -> deleteOrder(context, orderService,userService));
    }


    private static void showAdminPage(Context ctx, OrderService orderService, UserService userService) {

        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            ctx.status(403).result("Adgang nægtet");
            return;
        }

        List<Order> allOrders = orderService.getAllOrders();
        List<OrderLine> allOrderLines = orderService.getAllOrderLines();
        List<User> allUsers = userService.getAllUsers();

        ctx.attribute("currentUser", currentUser);
        ctx.attribute("orders", allOrders);
        ctx.attribute("orderLines", allOrderLines);
        ctx.attribute("users", allUsers);

        ctx.render("admin");
    }

    private static void addBalance(Context ctx, UserService userService) {

        User currentUser = ctx.sessionAttribute("currentUser");

        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            ctx.status(403).result("Adgang nægtet");
            return;
        }

        int userId = Integer.parseInt(ctx.formParam("userId"));
        double amount = Double.parseDouble(ctx.formParam("amount"));

        boolean success = userService.addBalance(userId, amount);

        if (success) {
            ctx.attribute("message", "Saldo opdateret!");
        } else {
            ctx.attribute("message", "Kunne ikke opdatere saldo");
        }

        ctx.redirect("/admin");
    }

    private static void deleteOrder(Context ctx, OrderService orderService, UserService userService){

        int orderId = Integer.parseInt(ctx.formParam("orderId"));
        orderService.deleteOrder(orderId);
        ctx.redirect("/admin");;
    }
}