package entities;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private List<OrderLine> orderLines = new ArrayList<>();
    private String pickupTime;

    public Order(int id, String pickupTime) {
        this.id = id;
        this.pickupTime = pickupTime;
    }

    public void addOrderLine(OrderLine orderLine){
        orderLines.add(orderLine);
    }

    public double getTotalPrice(){
        double total = 0;
        for(OrderLine line : orderLines){
            total += line.getLinePrice();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public String getPickupTime() {
        return pickupTime;
    }
}
