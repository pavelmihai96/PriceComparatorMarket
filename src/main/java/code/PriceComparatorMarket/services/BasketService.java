package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class BasketService {
    @Autowired
    private final ProductService productService;

    public Map<String, List<Product>> optimizeBasket(List<String> products, LocalDate date) {
        Map<String, List<Product>> listsOfProducts = new HashMap<>();

        for (String product : products) {
            productService.findBestOffer(product, date).ifPresent(
            i -> {
                listsOfProducts.computeIfAbsent(i.getStore(), j -> new ArrayList<>()).add(i);
            });
        }

        return listsOfProducts;
    }
}
