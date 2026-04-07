package entities;

import java.time.LocalDateTime;

public class OrderRequest {

    private int userId;
    private int bottomId;
    private int toppingId;
    private int quantity;
    private LocalDateTime pickupTime;

    public OrderRequest() {}

    public OrderRequest(int userId, int bottomId, int toppingId, int quantity, LocalDateTime pickupTime) {
        this.userId = userId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.quantity = quantity;
        this.pickupTime = pickupTime;
    }

    public int getUserId() {
        return userId;
    }

    public int getBottomId() {
        return bottomId;
    }

    public int getToppingId() {
        return toppingId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }
}