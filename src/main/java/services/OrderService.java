package services;

import entities.*;
import persistence.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = """
        SELECT o.id, o.user_id, u.email, o.pickup_time, o.total_price
        FROM orders o
        JOIN users u ON o.user_id = u.id
        WHERE o.user_id = ?
        ORDER BY o.pickup_time DESC
    """;

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setEmail(rs.getString("email"));
                    order.setPickupTime(rs.getTimestamp("pickup_time").toLocalDateTime());
                    order.setTotalPrice(rs.getDouble("total_price"));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<OrderLine> getAllOrderLines() {
        List<OrderLine> lines = new ArrayList<>();
        String sql = """
        SELECT ol.order_id, b.flavour AS bottomName, t.flavour AS topName,
               ol.quantity, ol.line_price
        FROM order_lines ol
        JOIN bottoms b ON ol.bottom_id = b.id
        JOIN toppings t ON ol.topping_id = t.id
    """;

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {
                OrderLine line = new OrderLine();
                line.setOrderId(rs.getInt("order_id"));
                line.setBottomFlavour(rs.getString("bottomName"));
                line.setTopFlavour(rs.getString("topName"));
                line.setQuantity(rs.getInt("quantity"));
                line.setLinePrice(rs.getDouble("line_price"));
                lines.add(line);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.user_id, u.email, o.pickup_time, o.total_price " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.id " +
                "ORDER BY o.pickup_time DESC";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setEmail(rs.getString("email"));
                order.setPickupTime(rs.getTimestamp("pickup_time").toLocalDateTime());
                order.setTotalPrice(rs.getDouble("total_price"));
                orders.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
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

    public void deleteOrder(int orderId){
        try(Connection connection = database.getConnection()){
            String deleteLinesSql = "DELETE FROM order_lines WHERE order_id = ?";
            String deleteOrderSql = "DELETE FROM orders WHERE id = ?";

            connection.setAutoCommit(false);

            PreparedStatement deleteLinePs = connection.prepareStatement(deleteLinesSql);
            deleteLinePs.setInt(1, orderId);
            deleteLinePs.executeUpdate();

            PreparedStatement deleteOrderPs = connection.prepareStatement(deleteOrderSql);
            deleteOrderPs.setInt(1, orderId);
            deleteOrderPs.executeUpdate();

            connection.commit();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}