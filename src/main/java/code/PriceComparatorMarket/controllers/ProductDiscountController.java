package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.requests.BasketRequest;
import code.PriceComparatorMarket.services.ProductDiscountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/product/discount")
public class ProductDiscountController {
    private final ProductDiscountService productDiscountService;

    @PostMapping("/highest")
    public List<ProductDiscount> getHighestProductDiscounts(@RequestBody LocalDate date) {
        return productDiscountService.getHighestProductDiscounts(date);
    }
}
