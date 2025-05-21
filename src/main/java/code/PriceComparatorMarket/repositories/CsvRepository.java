package code.PriceComparatorMarket.repositories;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CsvRepository<T> {
    List<T> loadAllProducts();

    List<T> loadLastProducts(Date date, Double hours);

    List<T> loadProductsByDate(LocalDate date);
}
