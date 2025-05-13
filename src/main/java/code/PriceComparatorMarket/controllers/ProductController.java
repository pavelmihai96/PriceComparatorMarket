package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.classes.Product;
import code.PriceComparatorMarket.services.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/product")
public class ProductController {
    @Autowired
    private final ProductService productService;

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

}
