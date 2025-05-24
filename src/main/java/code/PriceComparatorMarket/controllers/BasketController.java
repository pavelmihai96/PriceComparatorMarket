package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.requests.BasketRequest;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import code.PriceComparatorMarket.requests.ProductRequest;
import code.PriceComparatorMarket.services.BasketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/basket")
public class BasketController {
    private final BasketService basketService;

    @PostMapping("/cost-efficient")
    public ResponseEntity<?> costEfficient(@RequestBody BasketRequest request) {
        return basketService.costEfficient(request.getProductIds(), request.getDate());
    }

    @PostMapping("/best-buy")
    public ResponseEntity<?> bestBuy(@RequestBody ProductRequest request) {
        return basketService.bestBuy(request);
    }

    @PostMapping("/price-alert")
    public ResponseEntity<?> priceAlert(@RequestBody List<PriceAlertRequest> request) {
        return basketService.priceAlert(request);
    }
}
