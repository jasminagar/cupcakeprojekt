package controllers;

import entities.CupcakeBottom;
import entities.CupcakeTop;
import entities.OrderRequest;
import entities.User;
import io.javalin.Javalin;
import services.BottomService;
import services.ToppingService;
import services.UserService;

import java.util.List;

public class UserController {

    public UserController(Javalin app, UserService userService, BottomService bottomService, ToppingService toppingService) {

        app.get("/registerUser", ctx -> ctx.render("registerUser"));
        app.post("/registerUser", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            if (username == null || password == null) {
                ctx.status(400).result("Username and password required");
                return;
            }

            String trimUsername = username.trim();
            if (trimUsername.length() < 4) {
                ctx.status(400).result("Invalid username. Must be at least 4 characters");
                return;
            }

            boolean created = userService.createUser(trimUsername, password.trim());

            if (created) {
                ctx.status(201).result("User created successfully");
                ctx.redirect("/login");
            } else {
                ctx.status(409).result("Username already exists");
            }
        });
        //Til at liste alle brugere
        app.get("/users", ctx -> {
            ctx.attribute("users", userService.getAllUsers());
            ctx.render("users.html");
        });

        app.get("/users/{username}", ctx -> {
            String username = ctx.pathParam("username"); // virker som før
            var user = userService.getUserByUsername(username);

            if (user != null) {
                ctx.attribute("user", user);
                ctx.render("user");
            } else {
                ctx.status(404).result("User not found");
            }
        });
        app.get("/order", ctx -> {
            List<CupcakeBottom> bottoms = bottomService.getAllBottoms();
            List<CupcakeTop> toppings = toppingService.getAllTops();

            ctx.attribute("bottoms", bottoms);  // send til Thymeleaf
            ctx.attribute("toppings", toppings); // send til Thymeleaf
            ctx.attribute("orderRequest", new OrderRequest()); // til th:object

            User currentUser = ctx.sessionAttribute("currentUser");
            ctx.attribute("currentUser", currentUser); // hidden field

            ctx.render("order");
        });

        app.get("/login", context -> {
            context.render("login");
        });
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            if (username == null || password == null) {
                ctx.status(400).result("Username and password required");
                return;
            }

            boolean authenticated = userService.loginIn(username.trim(), password.trim());

            if (authenticated) {
                User user = userService.getUserByUsername(username.trim());
                ctx.sessionAttribute("currentUser", user);
                ctx.redirect("/order");
            } else {
                ctx.status(401).result("Invalid username or password");
            }
        });

    }
}