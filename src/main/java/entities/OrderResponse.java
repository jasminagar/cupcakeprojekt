package entities;

import java.time.LocalDateTime;

public class OrderResponse {

    private int orderId;
    private double totalPrice;
    private LocalDateTime pickupTime;

    public OrderResponse(int orderId, double totalPrice, LocalDateTime pickupTime) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.pickupTime = pickupTime;
    }

}