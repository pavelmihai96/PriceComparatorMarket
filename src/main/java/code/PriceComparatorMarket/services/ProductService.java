package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.repositories.CsvRepository;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import code.PriceComparatorMarket.requests.ProductCustom;
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

    public Optional<Product> findBestOffer(String productId, LocalDate date) {
        return productRepository.loadAllProducts().stream()
                .filter(p -> p.getProductId().equalsIgnoreCase(productId))
                /// Added this because it was possible to show a product from 1st when requesting on 8th, and it wasn't the case.
                /// e.g. assuming that the flyers are added once a week, if I request from frontend on 2025-05-03, I should be able to see the products also from 2025-05-01.
                /// But if I request from frontend on 2025-05-08, I shouldn't be able to see anything from 1st, because 7 days have past and new products are available.
                ///
                .filter(p -> !p.getDate().isAfter(date) && !p.getDate().isBefore(date.minusDays(6)))
                .peek(p -> productDiscountService.applyDiscount(p, date))
                .min(Comparator.comparing(Product::getPrice));
    }

    /// findBestValuePerUnit checks all products which are equal in terms of name, category and brand, regardless of id
    /// and it calculates for each of them the valuePerUnit.
    /// Then it retrieves the best value, which is the minimum.
    ///
    public Optional<Product> findBestValuePerUnit(ProductCustom request, LocalDate date) {
        return productRepository.loadAllProducts().stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(request.getProductName())
                            &&  p.getProductCategory().equalsIgnoreCase(request.getProductCategory())
                            &&  p.getBrand().equalsIgnoreCase(request.getBrand()))
                .filter(p -> !p.getDate().isAfter(date) && !p.getDate().isBefore(date.minusDays(6)))
                .peek(p -> {
                    productDiscountService.applyDiscount(p, date);
                    p.setValuePerUnit(calculateValuePerUnit(p.getPrice(), p.getPackageQuantity(), p.getPackageUnit()));
                })
                .min(Comparator.comparing(product -> Double.parseDouble(product.getValuePerUnit().split("\\sRON")[0])));
    }

    /// checkPriceAlert checks all products that have id equal to the one coming from request and applies the discount.
    /// Then it filters only the ones that have the prices lower or equal to the alert.
    /// Then it retrieves the mininum value based on price.
    ///
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

    /// calculateValuePerUnit method first transforms all other units to kg and l (e.g. ml, g)
    /// Then it calculates the ratio and returns a string
    ///
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
        return valuePerUnit + " RON / " + unit;
    }
}
