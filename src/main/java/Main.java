import config.SessionConfig;
import config.ThymeleafConfig;
import controllers.AdminController;
import controllers.OrderController;
import controllers.UserController;
import entities.Order;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import persistence.Database;
import services.BottomService;
import services.OrderService;
import services.ToppingService;
import services.UserService;

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


        UserService userService = new UserService(db);
        BottomService bottomService = new BottomService(db);
        ToppingService toppingService = new ToppingService(db);
        OrderService orderService = new OrderService(db, userService, bottomService, toppingService);
        AdminController adminController = new AdminController(app,userService,orderService);

        new UserController(app, userService);
        new OrderController(app, orderService, userService, bottomService, toppingService);
    }
}