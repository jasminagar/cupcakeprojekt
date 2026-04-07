package entities;

public class OrderLine {
    private CupcakeBottom bottom;
    private CupcakeTop top;
    private int quantity;

    public OrderLine(CupcakeBottom bottom, CupcakeTop top, int quantity) {
        this.bottom = bottom;
        this.top = top;
        this.quantity = quantity;
    }

    public double getLinePrice() {
        return (bottom.getPrice() + top.getPrice()) * quantity;
    }

    public CupcakeBottom getBottom() {
        return bottom;
    }

    public void setBottom(CupcakeBottom bottom) {
        this.bottom = bottom;
    }

    public CupcakeTop getTop() {
        return top;
    }

    public void setTop(CupcakeTop top) {
        this.top = top;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
