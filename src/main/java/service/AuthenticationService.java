package service;

import io.javalin.http.Context;

public class AuthenticationService {
//Kom til at validere i controller så fik chat til at hive det ud. Orkede ikke.

    public AuthenticationService() {
    }

    // === Hent input fra Context ===
    public String getUsername(Context ctx) {
        String username = ctx.formParam("username");
        if (username == null) throw new IllegalArgumentException("Username required");
        return username.trim();
    }

    public String getPassword(Context ctx) {
        String password = ctx.formParam("password");
        if (password == null) throw new IllegalArgumentException("Password required");
        return password.trim();
    }

    // === Validering ===
    public boolean isValidUsername(String username) {
        return username.length() >= 4;
    }

    // === Session handling ===
    public void createSession(Context ctx, String username) {
        ctx.sessionAttribute("currentUser", username);
    }

    // === Fejlhåndtering ===
    public boolean isBadRequest(Exception e) {
        return e instanceof IllegalArgumentException;
    }

    public void respondBadRequest(Context ctx, String message) {
        ctx.status(400).result(message);
    }

    public void respondUnauthorized(Context ctx) {
        ctx.status(401).result("Invalid username or password");
    }

    public void respondServerError(Context ctx, String message) {
        ctx.status(500).result(message);
    }
}