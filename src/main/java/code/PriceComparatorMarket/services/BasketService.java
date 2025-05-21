package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.requests.BasketRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class BasketService {
    @Autowired
    private final ProductService productService;

    public Map<String, List<Product>> costEfficient(List<String> products, LocalDate date) {
        Map<String, List<Product>> listsOfProducts = new HashMap<>();

        for (String product : products) {
            productService.findBestOffer(product, date).ifPresent(
            p -> {
                listsOfProducts.computeIfAbsent(p.getStore(), k -> new ArrayList<>()).add(p);
            });
        }

        return listsOfProducts;
    }

    public Map<String, List<Product>> bestBuy(List<String> products, LocalDate date) {
        return null;
    }
}
