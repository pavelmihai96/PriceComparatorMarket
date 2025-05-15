package code.PriceComparatorMarket.repositories;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.parsers.CsvParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class ProductCsvRepository implements CsvRepository<Product> {
    private final CsvParser<Product> parser;
    private final Path folder;

    public ProductCsvRepository(CsvParser<Product> parser, @Value("${csv.folder.path}") String folderPath) {
        this.parser = parser;
        this.folder = Path.of(folderPath);
    }

    @Override
    public List<Product> loadAll() {
        List<Product> results = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> results.addAll(parser.parse(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
}
