package services;

import entities.CupcakeBottom;
import persistence.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BottomService {
    private Database database;

    public BottomService(Database database) {
        this.database = database;
    }

    public List<CupcakeBottom> getAllBottoms() {
        List<CupcakeBottom> allBottoms = new ArrayList<>();

        try (Connection con = database.getConnection()) {
            String sql = "select * from bottoms";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allBottoms.add(new CupcakeBottom(
                        rs.getInt("id"),
                        rs.getString("flavour"),
                        rs.getInt("price")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return allBottoms;
    }

    public CupcakeBottom getBottomById(int id) {
        String sql = "select * from bottoms where id = ?";

        try (Connection con = database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CupcakeBottom(
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
