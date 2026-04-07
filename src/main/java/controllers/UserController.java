package controllers;

import io.javalin.Javalin;
import service.UserService;

public class UserController {

    public UserController(Javalin app, UserService userService) {

        app.get("/registerUser", ctx -> ctx.render("registerUser"));

        // POST /register - opret ny bruger
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
                ctx.redirect("/"); // tilbage til login siden
            } else {
                ctx.status(409).result("Username already exists");
            }
        });

        // GET /users - hent alle brugere (kun hvis du vil liste dem)
        app.get("/users", ctx -> {
            ctx.attribute("users", userService.getAllUsers());
            ctx.render("users.html"); // fil i templates
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
        app.get("/menu", context -> {
            context.render("menu");
        });
        // POST /login - log ind
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            if (username == null || password == null) {
                ctx.status(400).result("Username and password required");
                return;
            }

            boolean authenticated = userService.loginIn(username.trim(), password.trim());

            if (authenticated) {
                ctx.sessionAttribute("currentUser", username.trim());
                ctx.redirect("/menu");
            } else {
                ctx.status(401).result("Invalid username or password");
            }
        });

    }
}