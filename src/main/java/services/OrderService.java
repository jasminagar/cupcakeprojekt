package services;

import entities.*;
import persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class OrderService {

    private Database database;
    private BottomService bottomService;
    private ToppingService toppingService;
    private Database db;
    UserService userService = new UserService(db);

    public OrderService(Database database,UserService userService, BottomService bottomService, ToppingService toppingService) {
        this.database = database;
        this.userService = userService;
        this.bottomService = bottomService;
        this.toppingService = toppingService;
    }

    public OrderResponse createOrder(OrderRequest request) {

        CupcakeBottom bottom = bottomService.getBottomById(request.getBottomId());
        CupcakeTop topping = toppingService.getTopById(request.getToppingId());
        User user = userService.getUserById(request.getUserId());

        if (bottom == null || topping == null) {
            throw new IllegalArgumentException("Bottom or topping not found");
        }

        double totalPrice = (bottom.getPrice() + topping.getPrice()) * request.getQuantity();
        if(user.getBalance() < totalPrice){
            throw new IllegalArgumentException("Not enough funds");
        }

        try (Connection con = database.getConnection()) {

            con.setAutoCommit(false);

            String orderSql = """
                    insert into orders(user_id, pickup_time, total_price)
                    values (?, ?, ?)
                    returning id
                    """;

            PreparedStatement orderPs = con.prepareStatement(orderSql);
            orderPs.setInt(1, request.getUserId());
            orderPs.setTimestamp(2, Timestamp.valueOf(request.getPickupTime()));
            orderPs.setDouble(3, totalPrice);

            ResultSet rs = orderPs.executeQuery();
            rs.next();

            int orderId = rs.getInt("id");

            String lineSql = """
                    insert into order_lines(order_id, bottom_id, topping_id, quantity, line_price)
                    values (?, ?, ?, ?, ?)
                    """;

            PreparedStatement linePs = con.prepareStatement(lineSql);
            linePs.setInt(1, orderId);
            linePs.setInt(2, request.getBottomId());
            linePs.setInt(3, request.getToppingId());
            linePs.setInt(4, request.getQuantity());
            linePs.setDouble(5, totalPrice);
            linePs.executeUpdate();

            String updateBalanceSql = "update users set balance = balance - ? where id = ?";
            PreparedStatement balancePs = con.prepareStatement(updateBalanceSql);
            balancePs.setDouble(1, totalPrice);
            balancePs.setInt(2, request.getUserId());
            balancePs.executeUpdate();

            con.commit();

            return new OrderResponse(orderId, totalPrice, request.getPickupTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}