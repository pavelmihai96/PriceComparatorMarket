package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.requests.DiscountRequest;
import code.PriceComparatorMarket.services.ProductDiscountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/product/discount")
public class ProductDiscountController {
    private final ProductDiscountService productDiscountService;

    @GetMapping("/highest")
    public ResponseEntity<?> getHighestProductDiscounts(@RequestBody DiscountRequest request) {
        return productDiscountService.getHighestProductDiscounts(request);
    }

    @GetMapping("/hours")
    public ResponseEntity<?> getLastProductDiscounts(@RequestBody DiscountRequest request) {
        return productDiscountService.getLastProductDiscounts(request);
    }
}
