package code.PriceComparatorMarket.parsers;

import code.PriceComparatorMarket.models.ProductDiscount;
import com.opencsv.CSVReader;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Component
public class ProductDiscountCsvParser implements CsvParser<ProductDiscount> {

    @Override
    public List<ProductDiscount> parse(Path file) {
        //used to format de date coming from excel into LocalDate format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<ProductDiscount> discounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                if (line.length < 9) {
                    System.err.println("Invalid line in CSV file: " + String.join(",", line));
                    continue; // Skip invalid lines
                }
                try {
                    ProductDiscount d = new ProductDiscount();

                    d.setProductId(line[0]);
                    d.setProductName(line[1]);
                    d.setBrand(line[2]);
                    d.setPackageQuantity(Double.parseDouble(line[3]));
                    d.setPackageUnit(line[4]);
                    d.setProductCategory(line[5]);

                    d.setFromDate(LocalDate.parse(LocalDate.parse(line[6], inputFormatter).format(outputFormatter)));
                    d.setToDate(LocalDate.parse(LocalDate.parse(line[7], inputFormatter).format(outputFormatter)));
                    d.setPercentageOfDiscount(Double.parseDouble(line[8]));
                    d.setStore(getStoreName(file));
                    d.setDate(getDateFromFilename(file.getFileName().toString()));

                    discounts.add(d);
                } catch (NumberFormatException ex) {
                    System.err.println("From product discount: " + String.join(",", line));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discounts;
    }

    private String getStoreName(Path file) {
        return file.getFileName().toString().split("_")[0];
    }

    private LocalDate getDateFromFilename(String file) {
        String date = file.replaceAll(".*_(\\d{4}-\\d{2}-\\d{2})\\.csv", "$1");
        return LocalDate.parse(date);
    }
}
