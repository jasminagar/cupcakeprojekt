package controllers;

import entities.Order;
import entities.OrderLine;
import entities.User;
import io.javalin.Javalin;
import services.OrderService;
import services.UserService;

import java.util.List;

public class AdminController {
    public AdminController(Javalin app, UserService userService, OrderService orderService) {

        app.get("/admin", ctx -> {
            User currentUser = ctx.sessionAttribute("currentUser");
            if (currentUser == null || !"admin".equals(currentUser.getRole())) {
                ctx.status(403).result("Adgang nægtet");
                return;
            }

            // Hent ordrer og ordrelinjer
            List<Order> allOrders = orderService.getAllOrders();
            List<OrderLine> allOrderLines = orderService.getAllOrderLines();

            // Send til Thymeleaf
            ctx.attribute("currentUser", currentUser);
            ctx.attribute("orders", allOrders);
            ctx.attribute("orderLines", allOrderLines);

            ctx.render("admin"); // admin.html
        });

        app.post("/admin/addBalance", context -> {
            User currentUser = context.sessionAttribute("currentUser");
            if (currentUser == null || !"admin".equals(currentUser.getRole())) {
                context.status(403).result("Adgang nægtet");
                return;
            }

            int userId = Integer.parseInt(context.formParam("userId"));
            double amount = Double.parseDouble(context.formParam("amount"));

            boolean isSucces = userService.addBalance(userId, amount);

            if (isSucces) {
                context.result("Saldo opdateret for bruger " + currentUser.getName());
            } else {
                context.status(400).result("Kunne ikke opdaterer saldo");
            }
        });

    }
}
