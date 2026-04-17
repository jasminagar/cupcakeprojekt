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
        ctx.attribute("activeTab", "login");
        ctx.render("login");
    }

    private static void showRegister(Context ctx) {
        ctx.attribute("activeTab", "register");
        ctx.render("login");
    }

    private static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void login(Context ctx, UserService userService) {

        String email = ctx.formParam("email");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (email == null || username == null || password == null ||
                email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            ctx.attribute("message", "Email, username og password skal udfyldes");
            ctx.attribute("activeTab", "login");
            ctx.render("login");
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
            ctx.attribute("message", "Forkert mail, username eller kodeord");
            ctx.attribute("activeTab", "login");
            ctx.render("login");
        }
    }

    private static void register(Context ctx, UserService userService) {

        String email = ctx.formParam("email");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        String balanceParam = ctx.formParam("balance");

        if (email == null || username == null || password == null || balanceParam == null ||
                email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || balanceParam.trim().isEmpty()) {
            ctx.attribute("message", "Alle felter skal udfyldes");
            ctx.attribute("activeTab", "register");
            ctx.render("login");
            return;
        }

        if(userService.emailExists(email)){
            ctx.attribute("message", "Email findes allerede. Prøv igen");
            ctx.attribute("activeTab", "register");
            ctx.render("login");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceParam.trim());
        } catch (NumberFormatException e) {
            ctx.attribute("message", "Balance skal være et tal");
            ctx.attribute("activeTab", "register");
            ctx.render("login");
            return;
        }

        String trimEmail = email.trim();
        String trimUsername = username.trim();
        String trimPassword = password.trim();

        if (trimUsername.length() < 4) {
            ctx.attribute("message", "Username skal være mindst 4 tegn");
            ctx.attribute("activeTab", "register");
            ctx.render("login");
            return;
        }

        boolean created = userService.createUser(trimEmail, trimUsername, trimPassword, balance);

        if (created) {
            ctx.attribute("message", "Bruger oprettet! Du kan nu logge ind.");
            ctx.attribute("activeTab", "login");
            ctx.render("login");
        } else {
            ctx.attribute("message", "Brugeren findes allerede");
            ctx.attribute("activeTab", "register");
            ctx.render("login");
        }
    }
}