package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import code.PriceComparatorMarket.requests.ProductRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    public Optional<Product> findBestOffer(String productId, LocalDate date) {
        return productRepository.loadAllProducts().stream()
                .filter(p -> p.getProductId().equalsIgnoreCase(productId))

                /// added this because it was possible to show a product from 1st when requesting on 8th, and it wasn't the case
                .filter(p -> !p.getDate().isAfter(date) && !p.getDate().isBefore(date.minusDays(6)))
                /// .peek(p -> System.out.println("DEBUG---###After filter: " + p.getProductId() + ", " + p.getStore() + ", " + p.getDate() + ", " + p.getPrice() + "\n------"))
                .peek(p -> productDiscountService.applyDiscount(p, date))
                .min(Comparator.comparing(Product::getPrice));
    }

    public Optional<Product> findBestValuePerUnit(ProductRequest request) {
        return productRepository.loadAllProducts().stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(request.getProductName())
                            &&  p.getProductCategory().equalsIgnoreCase(request.getProductCategory())
                            &&  p.getBrand().equalsIgnoreCase(request.getBrand()))
                .peek(p -> System.out.println("After request filter: " + p.getProductId() + ", " + p.getProductName() + ", " + p.getPackageQuantity() + ", " + p.getPackageUnit() + ", " + p.getStore() + ", " + p.getDate()))
                .filter(p -> !p.getDate().isAfter(request.getDate()) && !p.getDate().isBefore(request.getDate().minusDays(6)))
                .peek(p -> {
                    productDiscountService.applyDiscount(p, request.getDate());
                    p.setValuePerUnit(calculateValuePerUnit(p.getPrice(), p.getPackageQuantity(), p.getPackageUnit()));
                })
                .min(Comparator.comparing(product -> Double.parseDouble(product.getValuePerUnit().split("\\sRON")[0])));
    }

    public Optional<Product> checkPriceAlert(PriceAlertRequest request, LocalDate date) {
        return productRepository.loadAllProducts().stream()
                .filter(p -> p.getProductId().equalsIgnoreCase(request.getProductId()))
                .filter(p -> !p.getDate().isAfter(date) && !p.getDate().isBefore(date.minusDays(6)))
                .peek(p -> {
                    productDiscountService.applyDiscount(p, date);
                })
                .filter(p -> p.getPrice() <= request.getPriceAlert())
                .min(Comparator.comparing(Product::getPrice));
    }

    public String calculateValuePerUnit(Double price, Double quantity, String unit) {
        if (Objects.equals(unit, "g")) {
            quantity /= 1000;
            unit = "kg";
        }
        if (Objects.equals(unit, "ml")) {
            quantity /= 1000;
            unit = "l";
        }

        Double valuePerUnit = price / quantity;
        return valuePerUnit + " RON/" + unit;
    }
}
