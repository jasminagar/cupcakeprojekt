package services;

import entities.CupcakeTop;
import persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ToppingService {
    private Database database;

    public ToppingService(Database database) {
        this.database = database;
    }

    public List<CupcakeTop> getAllTops() {
        List<CupcakeTop> allTops = new ArrayList<>();

        try (Connection con = database.getConnection()) {
            String sql = "select * from bottoms";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allTops.add(new CupcakeTop(
                        rs.getInt("id"),
                        rs.getString("flavour"),
                        rs.getInt("price")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allTops;
    }


    public CupcakeTop getTopById(int id) {
        String sql = "select * from toppings where id = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CupcakeTop(
                            rs.getInt("id"),
                            rs.getString("flavour"),
                            rs.getInt("price")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
