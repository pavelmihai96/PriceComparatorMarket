package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.requests.BasketRequest;
import code.PriceComparatorMarket.services.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    //call to this endpoint will optimize the basket and return the lists with store info and best prices
    @PostMapping("/costEfficient")
    public Map<String, List<Product>> optimize(@RequestBody BasketRequest request) {
        return basketService.optimizeBasket(request.getProductIds(), request.getDate());
    }
}
