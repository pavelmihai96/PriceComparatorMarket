package code.PriceComparatorMarket.repositories;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.parsers.CsvParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class ProductRepository implements CsvRepository<Product> {
    private final CsvParser<Product> parser;
    private final Path folder;

    public ProductRepository(CsvParser<Product> parser, @Value("${csv.folder.path}") String folderPath) {
        this.parser = parser;
        this.folder = Path.of(folderPath);
    }

    @Override
    public List<Product> loadAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> !p.toString().contains("discounts"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(parser.parse(p)));

            //results.forEach(p -> p.addToLocalDate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    @Override
    public List<Product> loadLastProducts(Date date, Double hours) {
        return null;
    }

    @Override
    public List<Product> loadProductsByDate(LocalDate date) {
        List<Product> allProducts = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains(date.toString()))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(parser.parse(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts;
    }
}
