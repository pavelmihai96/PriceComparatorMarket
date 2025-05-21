package code.PriceComparatorMarket.repositories;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.parsers.CsvParser;
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
import java.util.stream.Collectors;
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
            paths.peek(p -> System.out.println("P-ul inainte: " + p.toString()))
                    .filter(Files::isRegularFile)
                    .peek(p -> System.out.println("P-ul dupa: " + p.toString()))
                    .filter(p -> p.toString().contains("discounts"))
                    .filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> allProducts.addAll(parser.parse(p)));
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
                            //hours reprezinta orele care se trimit din frontend
                            //de exemplu se poate trimite valoarea 24,deci diferenta intre data la care se face interogarea si data la care fisierul a fost adaugat in folder
                            //trebuie sa fie mai mica sau egala cu numarul orelor * 60(minute)
                            return ((double) (date.getTime() - Files.readAttributes(p, BasicFileAttributes.class).creationTime().toMillis()) / (1000 * 60) <= (hours * 60));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(p -> allProducts.addAll(parser.parse(p)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allProducts;
    }

    @Override
    public List<ProductDiscount> loadProductsByDate(LocalDate date) {
        return null;
    }
}
