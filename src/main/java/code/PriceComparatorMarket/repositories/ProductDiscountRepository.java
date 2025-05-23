package code.PriceComparatorMarket.repositories;

import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.parsers.CsvParser;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.nio.file.attribute.BasicFileAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class ProductDiscountRepository implements CsvRepository<ProductDiscount> {
    private final CsvParser<ProductDiscount> reader;
    private final Path folder;

    public ProductDiscountRepository(CsvParser<ProductDiscount> reader, @Value("${csv.folder.path}") String folderPath) {
        this.reader = reader;
        this.folder = Path.of(folderPath);
    }

    @Override
    public List<ProductDiscount> loadAllProducts() {
        List<ProductDiscount> allProducts = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains("discounts"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(reader.read(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allProducts;
    }

    @Override
    public List<ProductDiscount> loadLastProducts(Date date, Double hours) {
        List<ProductDiscount> allProducts = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains("discounts"))
                    .filter(p -> {
                        try {
                            /// hours represents the hours sent from frontend
                            /// for instance, value 24 can be sent from frontend, so the difference between the date on which the request is made
                            /// and the date on which the file was added to the folder must be <= hours * 60
                            return ((double) (date.getTime() - Files.readAttributes(p, BasicFileAttributes.class).creationTime().toMillis()) / (1000 * 60) <= (hours * 60));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(p -> allProducts.addAll(reader.read(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allProducts;
    }

    /// not used
    @Override
    public List<ProductDiscount> loadProductsByDate(LocalDate date) {
        return null;
    }

    /// not used
    @Override
    public void updatePriceAlertCsv(List<PriceAlertRequest> request) {}
}
