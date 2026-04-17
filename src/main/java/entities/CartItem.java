package entities;

public class CartItem {
    private int bottomId;
    private int toppingId;
    private String bottomName;
    private String toppingName;
    private int quantity;
    private double totalPrice;

    public CartItem() {}

    public int getBottomId() {
        return bottomId;
    }

    public void setBottomId(int bottomId) {
        this.bottomId = bottomId;
    }

    public String getBottomName() {
        return bottomName;
    }

    public String getToppingName() {
        return toppingName;
    }

    public int getToppingId() {
        return toppingId;
    }

    public void setToppingId(int toppingId) {
        this.toppingId = toppingId;
    }

    public void setBottomName(String bottomName) {
        this.bottomName = bottomName;
    }

    public void setToppingName(String toppingName) {
        this.toppingName = toppingName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
