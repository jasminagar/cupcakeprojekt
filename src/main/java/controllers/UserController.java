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

    public UserController(Javalin app, UserService userService) {

        app.before(ctx -> {
            User currentUser = ctx.sessionAttribute("currentUser");
            if (currentUser != null) {
                ctx.attribute("currentUser", currentUser);
            }
        });

        app.get("/registerUser", ctx -> ctx.render("registerUser"));
        app.post("/registerUser", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            double balance = Double.parseDouble(ctx.formParam("balance"));

            if (username == null || password == null) {
                ctx.status(400).result("Username and password required");
                return;
            }

            String trimUsername = username.trim();
            if (trimUsername.length() < 4) {
                ctx.status(400).result("Invalid username. Must be at least 4 characters");
                return;
            }

            boolean created = userService.createUser(trimUsername, username.trim(), password.trim(), balance);

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

        app.get("/login", context -> {
            context.render("login");
        });
        app.post("/login", ctx -> {
            String email = ctx.formParam("email");
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            if (username == null || password == null) {
                ctx.status(400).result("Username and password required");
                return;
            }

            User user = userService.login(email.trim(), username.trim(), password.trim());

            if (user != null) {
                ctx.sessionAttribute("currentUser", user);

                if ("admin".equals(user.getRole())) {
                    ctx.redirect("/admin");
                } else {
                    ctx.redirect("/order");
                }

            } else {
                ctx.status(401).result("Forkert mail eller kdeord");
            }
        });

    }
}