import config.SessionConfig;
import config.ThymeleafConfig;
import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.Database;
import service.UserService;

public class Main {

    public static void main(String[] args) {
        Database db = new Database("postgres", "postgres", "jdbc:postgresql://localhost:5432/cupcake");

        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(
                    handler -> handler.setSessionHandler(SessionConfig.sessionConfig())
            );
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        app.get("/", ctx -> ctx.render("index"));

        // Opret services
        UserService userService = new UserService(db);

        // Opret controllers (registrerer endpoints)
        new UserController(app, userService);
    }
}