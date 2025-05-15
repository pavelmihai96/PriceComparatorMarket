package code.PriceComparatorMarket.parsers;

import code.PriceComparatorMarket.models.ProductDiscount;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Component
public class ProductDiscountCsvParser implements CsvParser<ProductDiscount> {

    @Override
    public List<ProductDiscount> parse(Path file) {
        List<ProductDiscount> discounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                ProductDiscount d = new ProductDiscount(
                        line[0], line[1], line[2],
                        Double.parseDouble(line[3]), line[4], line[5],
                        LocalDate.parse(line[6]), LocalDate.parse(line[7]),
                        Double.parseDouble(line[8]),
                        getStoreName(file)
                );
                discounts.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discounts;
    }

    private String getStoreName(Path file) {
        return file.getFileName().toString().split("_")[0];
    }
}
