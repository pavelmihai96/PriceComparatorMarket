package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ProductService {
    private final CsvRepository<Product> productRepository;
    private final CsvRepository<ProductDiscount> productDiscountRepository;
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

    public Optional<Product> findBestOffer(String productId, LocalDate date) {
        //parcurge toate produsele din csv-urile de forma store_date.csv
        Optional<Product> productWithBestPrice =  productRepository.loadAllProducts().stream()
                //.peek(p -> System.out.println("Inainte sa se filtreze dupa ID: " + p.getProductId()))
                .filter(p -> p.getProductId().equalsIgnoreCase(productId))
                //.peek(p -> System.out.println("Dupa ce s-a filtrat in functie de ID: " + p.getProductId() + ", " + p.getStore()))

                // added this because it was possible to show a product from 1st when requesting on 8th, and it wasn't the case
                .filter(p -> !p.getDate().isAfter(date) && !p.getDate().isBefore(date.minusDays(6)))
                .peek(p -> System.out.println("##Dupa ce s-a filtrat in functie de date: " + p.getProductId() + ", " + p.getStore() + ", " + p.getDate() + ", " + p.getPrice() + "\n------"))
                .peek(p -> productDiscountService.applyDiscount(p, date))

                //calculare minima in functie de data????
                .min(Comparator.comparing(Product::getPrice));

        return productWithBestPrice;
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
