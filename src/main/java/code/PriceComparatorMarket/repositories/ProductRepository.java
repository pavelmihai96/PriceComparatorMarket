package code.PriceComparatorMarket.repositories;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.parsers.CsvParser;
import code.PriceComparatorMarket.parsers.ProductCsvParser;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
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
    private final CsvParser<Product> reader;
    private final CsvParser<Product> writer;
    private final ProductCsvParser parser;
    private final Path folder;

    public ProductRepository(CsvParser<Product> reader, CsvParser<Product> writer, ProductCsvParser parser, @Value("${csv.folder.path}") String folderPath) {
        this.reader = reader;
        this.writer = writer;
        this.parser = parser;
        this.folder = Path.of(folderPath);
    }

    /// loadAllProducts method returns a list of Product objects after reading them with read method
    @Override
    public List<Product> loadAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> !p.toString().contains("discounts") && !p.toString().contains("price-alert"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(reader.read(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    /// loadProductsByDate method returns a list of Product objects after filtering them by the date which is part of the filename
    @Override
    public List<Product> loadProductsByDate(LocalDate date) {
        List<Product> allProducts = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains(date.toString()) && !p.toString().contains("discounts"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(reader.read(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    /// loadPriceAlerts method returns a list of PriceAlertRequest objects after reading them with readPriceAlert
    public List<PriceAlertRequest> loadPriceAlerts() {
        List<PriceAlertRequest> allProducts = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains("price-alert"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(parser.readPriceAlert(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allProducts;
    }

    /// updatePriceAlertCsv writes the list of PriceAlertRequest objects to price-alert.csv
    @Override
    public void updatePriceAlertCsv(List<PriceAlertRequest> request) {
        try (Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().contains("price-alert"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> writer.writePriceAlert(p, request));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// not used
    @Override
    public List<Product> loadLastProducts(Date date, Double hours) {
        return null;
    }
}