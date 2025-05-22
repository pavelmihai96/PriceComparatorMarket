package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.repositories.ProductRepository;
import code.PriceComparatorMarket.requests.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class BasketService {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final ProductDiscountService productDiscountService;

    @Autowired
    private final ProductRepository productRepository;

    public ResponseEntity<?> costEfficient(List<String> products, LocalDate date) {
        Map<String, List<Product>> listsOfProducts = new HashMap<>();

        for (String product : products) {
            productService.findBestOffer(product, date).ifPresent(
            p -> {
                listsOfProducts.computeIfAbsent(p.getStore(), k -> new ArrayList<>()).add(p);
            });
        }

        if (listsOfProducts.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Couldn't find the requested products."));
        } else {
            return ResponseEntity.ok().body(listsOfProducts);
        }
    }

    public ResponseEntity<?> bestBuy(List<ProductRequest> request) {
        Map<String, List<Product>> listsOfProducts = new HashMap<>();

        for (ProductRequest product : request) {
            productService.findBestValuePerUnit(product).ifPresent(
                    p -> {
                        listsOfProducts.computeIfAbsent(p.getStore(), k -> new ArrayList<>()).add(p);
                    });
        }

        if (listsOfProducts.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Couldn't find the requested products."));
        } else {
            return ResponseEntity.ok().body(listsOfProducts);
        }

    }
}
