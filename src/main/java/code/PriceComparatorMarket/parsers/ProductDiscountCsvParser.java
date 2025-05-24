package code.PriceComparatorMarket.parsers;

import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;
import java.time.format.DateTimeFormatter;

import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Component
public class ProductDiscountCsvParser implements CsvParser<ProductDiscount> {
    /// read method reads the csv and maps it to a ProductDiscount object
    @Override
    public List<ProductDiscount> read(Path file) {
        List<ProductDiscount> discounts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                if (line.length < 9) {
                    System.err.println("ProductDiscountCsvParser-Invalid line in CSV file: " + String.join(",", line));
                    continue; // Skip invalid lines
                }
                try {
                    discounts.add(createProductFromLine(line, file));
                } catch (NumberFormatException ex) {
                    System.err.println("ProductDiscountCsvParser-From product discount: " + String.join(",", line));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return discounts;
    }

    /// Method to create a new Product object and store data from csv in it
    public ProductDiscount createProductFromLine(String[] line, Path file) {
        /// Used to format de date coming from excel into LocalDate format
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        ProductDiscount pd = new ProductDiscount();

        pd.setProductId(line[0]);
        pd.setProductName(line[1]);
        pd.setBrand(line[2]);
        pd.setPackageQuantity(Double.parseDouble(line[3]));
        pd.setPackageUnit(line[4]);
        pd.setProductCategory(line[5]);
        pd.setFromDate(LocalDate.parse(LocalDate.parse(line[6], inputFormatter).format(outputFormatter)));
        pd.setToDate(LocalDate.parse(LocalDate.parse(line[7], inputFormatter).format(outputFormatter)));
        pd.setPercentageOfDiscount(Double.parseDouble(line[8]));
        pd.setStore(getStoreName(file));
        pd.setDate(getDateFromFilename(file.getFileName().toString()));
        return pd;
    }

    private String getStoreName(Path file) {
        return file.getFileName().toString().split("_")[0];
    }

    private LocalDate getDateFromFilename(String file) {
        String date = file.replaceAll(".*_(\\d{4}-\\d{2}-\\d{2})\\.csv", "$1");
        return LocalDate.parse(date);
    }

    ///  not used
    @Override
    public void writePriceAlert(Path file, List<PriceAlertRequest> request) {}
}
