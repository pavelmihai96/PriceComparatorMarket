package code.PriceComparatorMarket.parsers;

import code.PriceComparatorMarket.requests.PriceAlertRequest;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
public interface CsvParser<T> {
    List<T> read(Path file);

    void write(Path file, List<PriceAlertRequest> request);
}
