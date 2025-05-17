package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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


                listPD.stream()
                        .max(Comparator.comparing(ProductDiscount::getDate))
                        .ifPresent(d -> p.setPrice(p.getPrice() * (1 - d.getPercentageOfDiscount() / 100.0)));
    }
}
