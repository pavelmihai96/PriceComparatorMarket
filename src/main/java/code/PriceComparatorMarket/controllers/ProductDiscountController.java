package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.requests.BasketRequest;
import code.PriceComparatorMarket.services.ProductDiscountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/product/discount")
public class ProductDiscountController {
    private final ProductDiscountService productDiscountService;

    @PostMapping("/highest")
    public ResponseEntity<?> getHighestProductDiscounts(@RequestBody LocalDate date) {
        return productDiscountService.getHighestProductDiscounts(date);
    }

    @GetMapping("/last/{xHours}")
    public ResponseEntity<?> getLastProductDiscounts(@PathVariable Double xHours) {
        return productDiscountService.getLastProductDiscounts(xHours);
    }
}
