package code.PriceComparatorMarket.repositories;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CsvRepository<T> {
    List<T> loadAll();
}
