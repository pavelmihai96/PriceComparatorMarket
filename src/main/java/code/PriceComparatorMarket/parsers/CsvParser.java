package code.PriceComparatorMarket.parsers;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Component
public interface CsvParser<T> {
    List<T> parse(Path file);
}
