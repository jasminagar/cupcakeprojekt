import config.SessionConfig;
import config.ThymeleafConfig;
import controllers.AdminController;
import controllers.OrderController;
import controllers.UserController;
import entities.User;
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

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(
                    handler -> handler.setSessionHandler(SessionConfig.sessionConfig())
            );
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        app.before(ctx -> {
            User user = ctx.sessionAttribute("currentUser");
            if (user != null) {
                ctx.attribute("currentUser", user);
            }
        });

        app.before("/order", ctx -> {
            if (ctx.sessionAttribute("currentUser") == null) {
                ctx.redirect("/login");
            }
        });

        app.before("/admin", ctx -> {
            User user = ctx.sessionAttribute("currentUser");
            if (user == null || !"admin".equals(user.getRole())) {
                ctx.redirect("/login");
            }
        });

        UserService userService = new UserService(db);
        BottomService bottomService = new BottomService(db);
        ToppingService toppingService = new ToppingService(db);
        OrderService orderService = new OrderService(db, userService, bottomService, toppingService);

        app.get("/", ctx -> ctx.render("index.html"));

        AdminController.addRoutes(app, userService, orderService);
        OrderController.addRoutes(app, orderService, userService, bottomService, toppingService);
        UserController.addRoutes(app, userService);
    }
}