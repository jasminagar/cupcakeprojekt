package services;

import entities.CupcakeBottom;
import entities.CupcakeTop;

public class CupcakeService {
private BottomService bottomService;
private ToppingService toppingService;

    public CupcakeService(BottomService bottomService, ToppingService toppingService) {
        this.bottomService = bottomService;
        this.toppingService = toppingService;
    }


    public double calculatePrice(int bottomId, int toppingId, int quantity) {
        CupcakeBottom bottom = bottomService.getBottomById(bottomId);
        CupcakeTop top = toppingService.getTopById(toppingId);
        return (bottom.getPrice() + top.getPrice()) * quantity;
    }
}
