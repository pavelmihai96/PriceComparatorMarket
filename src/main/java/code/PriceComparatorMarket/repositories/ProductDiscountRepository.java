package code.PriceComparatorMarket.repositories;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
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
public class ProductDiscountRepository implements CsvRepository<ProductDiscount> {
    private final CsvParser<ProductDiscount> parser;
    private final Path folder;

    public ProductDiscountRepository(CsvParser<ProductDiscount> parser, @Value("${csv.folder.path}") String folderPath) {
        this.parser = parser;
        this.folder = Path.of(folderPath);
    }

    @Override
    public List<ProductDiscount> loadAllProducts() {
        List<ProductDiscount> allProducts = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains("discounts"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(parser.parse(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println("Loaded discounts: " + allProducts.size());
//        allProducts.forEach(d -> System.out.println(
//                "Discount loaded: " + d.getProductId() + ", " + d.getStore() + ", " + d.getFromDate() + " - " + d.getToDate() + "; " + d.getDate()
//        ));

        return allProducts;
    }
}
