package service;

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

    public boolean createUser(String name, String password) {
        try (Connection connection = database.getConnection()) {
            String sql = "insert into users (name, password) values (?,?)";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, password);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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
                        rs.getString("password")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } return allUsers;
    }

    public List<User> getUserByUsername(String username){
        List<User> user = new ArrayList<>();
        try(Connection connection = database.getConnection()){
            String sql = "select * from users where name = ?";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } return user;
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
