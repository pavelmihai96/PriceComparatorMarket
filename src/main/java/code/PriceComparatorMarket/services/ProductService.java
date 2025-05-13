package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.classes.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import java.io.FileReader;

@Service
public class ProductService {

    public Product getProduct(String productId) {
        Product product = new Product();

        try (CSVReader reader = new CSVReader(new FileReader("src/main/java/code/PriceComparatorMarket/data/lidl_2025-05-01.csv"))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (productId.equals(nextLine[0])) {
                    product.setProduct_id(nextLine[0]);
                    product.setProduct_name(nextLine[1]);
                    product.setProduct_category(nextLine[2]);
                    product.setBrand(nextLine[3]);
                    product.setPackage_quantity(Double.parseDouble(nextLine[4]));
                    product.setPackage_unit(nextLine[5]);
                    product.setPrice(Double.parseDouble(nextLine[6]));
                    product.setCurrency(nextLine[7]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }
}
