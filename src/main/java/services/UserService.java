package services;

import entities.User;
import persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Database database;

    public UserService(Database database) {
        this.database = database;
    }

    public boolean addBalance(int userId, double amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, userId);

            int updated = ps.executeUpdate();
            return updated == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createUser(String email, String name, String password, double balance) {
        try (Connection connection = database.getConnection()) {
            String sql = "insert into users (email, name, password, balance) values (?, ?,?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setDouble(4, balance);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public User getUserById(int id) {
        String sql = "SELECT id, email, name, password, balance, role FROM users WHERE id = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getDouble("balance"),
                            rs.getString("role")
                    );

                    user.setId(rs.getInt("id"));

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User login(String email, String username, String password) {
        String sql = "SELECT id, email, name, password, balance, role FROM users WHERE email = ? and name = ? AND password = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("email"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getDouble("balance"),
                            rs.getString("role")
                    );
                    user.setId(rs.getInt("id"));
                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login fejlede
    }

    public User getUserByUsername(String username) {
        User user = null;
        try (Connection connection = database.getConnection()) {
            String sql = "SELECT * FROM users WHERE name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean loginIn(String name, String password) {
        try (Connection connection = database.getConnection()) {
            String sql = "select * from users where name = ? and password = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("USER FOUND");
            } else {
                System.out.println("USER NOT FOUND");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
