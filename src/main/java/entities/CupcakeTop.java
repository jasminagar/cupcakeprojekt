package entities;

public class CupcakeTop {
    private int id;
    private String flavour;
    private double price;

    public CupcakeTop(int id, String flavour, double price) {
        this.id = id;
        this.flavour = flavour;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlavour() {
        return flavour;
    }

    public double getPrice() {
        return price;
    }

}
