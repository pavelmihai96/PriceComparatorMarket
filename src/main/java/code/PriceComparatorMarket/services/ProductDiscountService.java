package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ProductDiscountService {
    private final CsvRepository<ProductDiscount> productDiscountRepository;

    public void applyDiscount(Product p, LocalDate date) {
        System.out.println("Inainte de apelul loadAllProducts pentru dicounts!");

        List<ProductDiscount> listPD = productDiscountRepository.loadAllProducts().stream()
                .peek(d -> System.out.println("############Discount inainte de filtrare cu id si magazin: " + d.getProductId() + ", " + d.getStore() + ", " + d.getFromDate() + ", " + d.getToDate() + ", " + d.getDate() + ", " + d.getPercentageOfDiscount()))
                //.peek(d -> System.out.println("Store discount: " + d.getStore() + ", Store product: " + p.getStore()))
                .filter(d -> d.getProductId().equalsIgnoreCase(p.getProductId()))
                .filter(d -> d.getStore().equalsIgnoreCase(p.getStore()))
                .filter(d -> !date.isBefore(d.getFromDate()) && !date.isAfter(d.getToDate()))
                .peek(d -> System.out.println("############Discount dupa filtrare cu id si magazin si perioada: " + d.getProductId() + ", " + d.getStore() + ", " + d.getFromDate() + ", " + d.getToDate() + ", " + d.getDate() + ", " + d.getPercentageOfDiscount()))
                .collect(Collectors.toList());


                //primul gasit care indeplineste filtrele de mai sus incheie streamul curent si trece la urmatorul ProductDiscount prin callul findFirst()
//                .findFirst()
//                .ifPresent(d -> p.setPrice(p.getPrice() * (1 - d.getPercentageOfDiscount() / 100.0)));

                //aplica discount pentru cel mai recent produs, si pare sa il ia pe primul in cazul in care sunt duplicate
                listPD.stream()
                        .max(Comparator.comparing(ProductDiscount::getDate))
                        .ifPresent(d -> p.setPrice(p.getPrice() * (1 - d.getPercentageOfDiscount() / 100.0)));
    }

    public ResponseEntity<?> getHighestProductDiscounts(LocalDate date) {
        List<ProductDiscount> listPD = productDiscountRepository.loadAllProducts().stream()
                .filter(pd -> !pd.getFromDate().isAfter(date) && !pd.getToDate().isBefore(date))
                .peek(pd -> System.out.println("Product after date filter: " + pd.getProductId() + ", " + pd.getFromDate() + ", " + pd.getToDate() + ", Date of file: " + pd.getDate()))
                .collect(Collectors.toList());

        List<ProductDiscount> listPDSorted = listPD.stream()
                .sorted(Comparator.comparing(ProductDiscount::getPercentageOfDiscount).reversed())
                .collect(Collectors.toList());

        //return a message if not discounts are available; and discounts otherwise
        if (listPDSorted.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No discounts were found on date: " + date.toString()));
        } else  {
            return ResponseEntity.ok().body(listPDSorted);
        }
    }

    public ResponseEntity<?> getLastProductDiscounts(Double xHours) {
        Date currentDate = new Date();
        List<ProductDiscount> listPD = productDiscountRepository.loadLastProducts(currentDate, xHours);
        Map<String, List<ProductDiscount>> listPDMap = new HashMap<>();

        //setting a map of Store and Products with discounts before returning to the user
        listPD.forEach(pd -> {
                    listPDMap.computeIfAbsent(pd.getStore(), k -> new ArrayList<>()).add(pd);
                });

        //return a message if no discounts are available; and discounts otherwise
        if (listPD.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No discounts were found on the last " + xHours + " hours."));
        } else {
            return ResponseEntity.ok().body(listPDMap);
        }

    }
}
