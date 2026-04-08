package controllers;

import entities.User;
import io.javalin.Javalin;
import services.OrderService;
import services.UserService;

public class AdminController {
    public AdminController(Javalin app, UserService userService, OrderService orderService) {

        app.get("/admin", context -> {
            User currentUser = context.sessionAttribute("currentUser");
            if (currentUser == null || !"admin".equals(currentUser.getRole())) {
                context.status(403).result("Adgang nægtet");
                return;
            }
            context.render("admin");
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
