package code.PriceComparatorMarket.parsers;

import code.PriceComparatorMarket.models.Product;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCsvParser implements CsvParser<Product> {

    @Override
    public List<Product> parse(Path file) {
        List<Product> products = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                Product p = new Product(
                        line[0], line[1], line[2], line[3],
                        Double.parseDouble(line[4]), line[5],
                        Double.parseDouble(line[6]), line[7],
                        getDateFromFilename(file.getFileName().toString()),
                        getStoreName(file)
                );
                products.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private String getStoreName(Path file) {
        return file.getFileName().toString().split("_")[0];
    }

    private LocalDate getDateFromFilename(String file) {
        String date = file.replaceAll(".*_(\\d{4}-\\d{2}-\\d{2})\\.csv", "$1");
        return LocalDate.parse(date);
    }
}
