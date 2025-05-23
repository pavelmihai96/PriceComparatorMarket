package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.repositories.ProductRepository;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import code.PriceComparatorMarket.requests.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@AllArgsConstructor
@Service
public class BasketService {

    @Autowired
    private final ProductService productService;

    @Autowired
    private final ProductRepository productRepository;

    /// costEfficient method will return a Map with a pair of String and Product if there is anything found or a message if not
    /// 1. First it is a loop over all the objects sent from frontend
    /// 2. then for each object findBestOffer is called, which verifies the best offer in terms of price, if it exists
    /// 3. then that Product will be mapped
    /// 4. the method will return the Map or a message
    public ResponseEntity<?> costEfficient(List<String> products, LocalDate date) {
        Map<String, List<Product>> listsOfProducts = new HashMap<>();

        for (String product : products) {
            productService.findBestOffer(product, date).ifPresent(
            p -> {
                /// add the pair { "store": Product }
                listsOfProducts.computeIfAbsent(p.getStore(), k -> new ArrayList<>()).add(p);
            });
        }

        /// check if there is anything to be returned from listsOfProducts
        if (listsOfProducts.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Couldn't find the requested products."));
        } else {
            return ResponseEntity.ok().body(listsOfProducts);
        }
    }

    /// bestBuy method will return a Map with a pair of String and Product if there is anything found or a message if not
    /// 1. First it is a loop over all the objects sent from frontend
    /// 2. then for each object findBestValuePerUnit is called, which verifies the best value per unit, if it exists
    /// 3. then that Product will be mapped
    /// 4. the method will return the Map or a message
    public ResponseEntity<?> bestBuy(List<ProductRequest> request) {
        Map<String, List<Product>> listsOfProducts = new HashMap<>();

        for (ProductRequest product : request) {
            productService.findBestValuePerUnit(product).ifPresent(
                    p -> {
                        /// add the pair { "store": Product }
                        listsOfProducts.computeIfAbsent(p.getStore(), k -> new ArrayList<>()).add(p);
                    });
        }

        /// check if there is anything to be returned from listsOfProducts
        if (listsOfProducts.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Couldn't find the requested products."));
        } else {
            return ResponseEntity.ok().body(listsOfProducts);
        }

    }

    /// priceAlert method will return a Map with a pair of String, and either a Product, or a String in case it didn't find a price matching or lower than the target
    /// 1. First it is a loop over all the objects sent from frontend
    /// 2. then for each object checkPriceAlert is called, which verifies if there is a product with the given priceAlert
    /// 3. then that Product will be mapped, otherwise a message will be mapped
    public ResponseEntity<?> priceAlert(List<PriceAlertRequest> request) {
        productRepository.updatePriceAlertCsv(request);

        Date currentDate = new Date();
        Map<String, List<Object>> listsOfProducts = new HashMap<>();

        for (PriceAlertRequest pr : request) {
            productService.checkPriceAlert(pr, currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).ifPresent(
                    p -> {
                        /// add the pair { "store": Product }
                        listsOfProducts.computeIfAbsent(p.getStore(), k -> new ArrayList<>()).add(p);
                    });

            /// add the pair { "productId" : message }
            if (productService.checkPriceAlert(pr, currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).isEmpty()) {
                listsOfProducts.computeIfAbsent(pr.getProductId(), k -> new ArrayList<>()).add("Price didn't reach the set alert.");
            }
        }

        return ResponseEntity.ok().body(listsOfProducts);
    }
}
