package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.repositories.CsvRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ProductService {
    private final CsvRepository<Product> productRepository;
    private final ProductDiscountService productDiscountService;

    public Product getProduct(String productId) {
        Product product = new Product();

        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/code/PriceComparatorMarket/data/lidl_2025-05-01.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (productId.equals(nextLine[0])) {
                    product.setProductId(nextLine[0]);
                    product.setProductName(nextLine[1]);
                    product.setProductCategory(nextLine[2]);
                    product.setBrand(nextLine[3]);
                    product.setPackageQuantity(Double.parseDouble(nextLine[4]));
                    product.setPackageUnit(nextLine[5]);
                    product.setPrice(Double.parseDouble(nextLine[6]));
                    product.setCurrency(nextLine[7]);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    public Optional<Product> findBestOffer(String name, LocalDate date) {
        return productRepository.loadAll().stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(name))
                .filter(p -> p.getDate().equals(date))
                .peek(p -> productDiscountService.applyDiscount(p, date))
                .min(Comparator.comparing(Product::getPrice));
    }
/*
    public void addItemsToBasket() {
        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/code/PriceComparatorMarket/data/Basket.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[2].equals("TRUE")) {
                    this.basket.add(nextLine[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 */
}
