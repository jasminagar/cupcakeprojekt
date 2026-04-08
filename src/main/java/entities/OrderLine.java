package entities;

public class OrderLine {
    private int id;
    private int orderId;
    private int bottomId;
    private int toppingId;
    private String bottomFlavour;
    private String topFlavour;
    private int quantity;
    private double linePrice;

    public OrderLine() {
    }

    public OrderLine(int id, int orderId, int bottomId, int toppingId, String bottomFlavour, String topFlavour, int quantity, double linePrice) {
        this.id = id;
        this.orderId = orderId;
        this.bottomId = bottomId;
        this.toppingId = toppingId;
        this.bottomFlavour = bottomFlavour;
        this.topFlavour = topFlavour;
        this.quantity = quantity;
        this.linePrice = linePrice;
    }

    // getters og setters
    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getBottomId() { return bottomId; }
    public int getToppingId() { return toppingId; }
    public String getBottomFlavour() { return bottomFlavour; }
    public String getTopFlavour() { return topFlavour; }
    public int getQuantity() { return quantity; }
    public double getLinePrice() { return linePrice; }

    public void setId(int id) { this.id = id; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setBottomId(int bottomId) { this.bottomId = bottomId; }
    public void setToppingId(int toppingId) { this.toppingId = toppingId; }
    public void setBottomFlavour(String bottomFlavour) { this.bottomFlavour = bottomFlavour; }
    public void setTopFlavour(String topFlavour) { this.topFlavour = topFlavour; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setLinePrice(double linePrice) { this.linePrice = linePrice; }
}