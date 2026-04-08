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

    public boolean addBalance(int userId, double amount){
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

    public boolean createUser(String name, String password, double balance) {
        try (Connection connection = database.getConnection()) {
            String sql = "insert into users (name, password, balance) values (?,?, ?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setDouble(3, balance);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public User getUserById(int id) {
        String sql = "SELECT id, name, password, balance, role FROM users WHERE id = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
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

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();

        try (Connection connection = database.getConnection()) {
            String sql = "select * from users";

            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allUsers.add(new User(
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getDouble("balance"),
                        rs.getString("role")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allUsers;
    }

    public User login(String username, String password) {
        String sql = "SELECT id, name, password, balance, role FROM users WHERE name = ? AND password = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
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
