package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import code.PriceComparatorMarket.requests.DiscountRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ProductDiscountService {
    private final CsvRepository<ProductDiscount> productDiscountRepository;

    public void applyDiscount(Product p, LocalDate date) {
        List<ProductDiscount> listPD = productDiscountRepository.loadAllProducts().stream()
                .filter(d -> d.getProductId().equalsIgnoreCase(p.getProductId()))
                .filter(d -> d.getStore().equalsIgnoreCase(p.getStore()))
                .filter(d -> !date.isBefore(d.getFromDate()) && !date.isAfter(d.getToDate()))
                .toList();

        /// applies the discount for the most recent product, and in case there are duplicates, it takes the first one
        listPD.stream()
                .max(Comparator.comparing(ProductDiscount::getDate))
                .ifPresent(d -> p.setPrice(p.getPrice() * (1 - d.getPercentageOfDiscount() / 100.0)));
    }

    public ResponseEntity<?> getHighestProductDiscounts(DiscountRequest request) {
        List<ProductDiscount> listPD = productDiscountRepository.loadAllProducts().stream()
                .filter(pd -> !pd.getFromDate().isAfter(request.getDate()) && !pd.getToDate().isBefore(request.getDate()))
                .toList();

        List<ProductDiscount> listPDSorted = listPD.stream()
                .sorted(Comparator.comparing(ProductDiscount::getPercentageOfDiscount).reversed())
                .toList();

        /// return a message if not discounts are available; and discounts otherwise
        if (listPDSorted.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No discounts were found on date: " + request.getDate().toString()));
        } else  {
            return ResponseEntity.ok().body(listPDSorted);
        }
    }

    public ResponseEntity<?> getLastProductDiscounts(DiscountRequest request) {
        Date currentDate = new Date();
        List<ProductDiscount> listPD = productDiscountRepository.loadLastProducts(currentDate, request.getHours());
        Map<String, List<ProductDiscount>> listPDMap = new HashMap<>();

        /// setting a map of Store and Products with discounts before returning to the user
        listPD.forEach(pd -> {
                    listPDMap.computeIfAbsent(pd.getStore(), k -> new ArrayList<>()).add(pd);
                });

        /// return a message if no discounts are available; and discounts otherwise
        if (listPD.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No discounts were found on the last " + request.getHours() + " hours."));
        } else {
            return ResponseEntity.ok().body(listPDMap);
        }

    }
}
