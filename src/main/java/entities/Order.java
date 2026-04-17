package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int userId;
    private String email;
    private double totalPrice;
    private List<OrderLine> orderLines = new ArrayList<>();
    private LocalDateTime pickupTime;

    public Order(int id, int userId, String email, List<OrderLine> orderLines, LocalDateTime pickupTime) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.orderLines = orderLines;
        this.pickupTime = pickupTime;
    }

    public Order(int id, LocalDateTime pickupTime) {
        this.id = id;
        this.pickupTime = pickupTime;
    }

    public Order() {

    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
