package code.PriceComparatorMarket.parsers;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCsvParser implements CsvParser<Product> {

    @Override
    public List<Product> read(Path file) {

        List<Product> products = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(file.toFile()))) {
            String[] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                if (line.length < 8) {
                    System.err.println("ProductCsvParser: Invalid line in CSV file: " + String.join(",", line));
                    continue; /// Skip invalid lines
                }
                try {
                    products.add(createProductFromLine(line, file));
                } catch (NumberFormatException ex) {
                    System.err.println("From product: " + String.join(",", line));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void write(Path file, List<PriceAlertRequest> request) {

        try (CSVWriter writer = new CSVWriter(
                new FileWriter(file.toFile()),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END))
        {
            String[] header = { "productId", "priceAlert" };

            writer.writeNext(header);
            for (PriceAlertRequest pr: request) {
                String[] row = {pr.getProductId(), String.valueOf(pr.getPriceAlert())};
                writer.writeNext(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /// Method to create a new Product object and store data from csv in it
    public Product createProductFromLine(String[] line, Path file) {
        Product p = new Product();
        p.setProductId(line[0]);
        p.setProductName(line[1]);
        p.setProductCategory(line[2]);
        p.setBrand(line[3]);
        p.setPackageQuantity(Double.parseDouble(line[4]));
        p.setPackageUnit(line[5]);
        p.setPrice(Double.parseDouble(line[6]));
        p.setCurrency(line[7]);
        p.setDate(getDateFromFilename(file.getFileName().toString()));
        p.setStore(getStoreName(file));
        return p;
    }

    private String getStoreName(Path file) {
        return file.getFileName().toString().split("_")[0];
    }

    private LocalDate getDateFromFilename(String file) {
        String date = file.replaceAll(".*_(\\d{4}-\\d{2}-\\d{2})\\.csv", "$1");
        return LocalDate.parse(date);
    }
}
