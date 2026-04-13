package controllers;

import entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import services.UserService;

public class UserController {

    public static void addRoutes(Javalin app, UserService userService) {

        app.get("/login", ctx -> showLogin(ctx));
        app.post("/login", ctx -> login(ctx, userService));
        app.get("/logout", ctx -> logout(ctx));

        app.get("/registerUser", ctx -> showRegister(ctx));
        app.post("/registerUser", ctx -> register(ctx, userService));
    }

    private static void showLogin(Context ctx) {
        ctx.render("login");
    }

    private static void showRegister(Context ctx) {
        ctx.render("registerUser");
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void login(Context ctx, UserService userService) {

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

//********Med i rapporten/sql med***********
            if ("admin".equals(user.getRole())) {
                ctx.redirect("/admin");
            } else {
                ctx.redirect("/order");
            }
        } else {
            ctx.attribute("message", "Forkert mail eller kodeord");
            ctx.render("login");
        }
    }

    private static void register(Context ctx, UserService userService) {

        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        double balance = Double.parseDouble(ctx.formParam("balance"));

        if (username == null || password == null) {
            ctx.status(400).result("Username and password required");
            return;
        }

        String trimUsername = username.trim();

        if (trimUsername.length() < 4) {
            ctx.attribute("message", "Username skal være mindst 4 tegn");
            ctx.render("registerUser");
            return;
        }

        boolean created = userService.createUser(trimUsername, trimUsername, password.trim(), balance);

        if (created) {
            ctx.redirect("/login");
        } else {
            ctx.attribute("message", "Username findes allerede");
            ctx.render("registerUser");
        }
    }
}