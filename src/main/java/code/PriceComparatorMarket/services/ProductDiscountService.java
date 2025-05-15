package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@Service
public class ProductDiscountService {
    private final CsvRepository<ProductDiscount> productDiscountRepository;

    public void applyDiscount(Product p, LocalDate date) {
        productDiscountRepository.loadAll().stream()
                .filter(d -> d.getProductName().equalsIgnoreCase(p.getProductName()))
                .filter(d -> d.getStore().equalsIgnoreCase(p.getStore()))
                .filter(d -> !date.isBefore(d.getFromDate()) && !date.isAfter(d.getToDate()))
                .findFirst()
                .ifPresent(d -> p.setPrice(p.getPrice() * (1 - d.getPercentageOfDiscount() / 100.0)));
    }
}
