package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
