package code.PriceComparatorMarket.models;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private Double packageQuantity;
    private String packageUnit;
    private Double price;
    private String currency;
    private LocalDate date;
    private String store;

    //This is added because I want to store multiple dates into date variable
    //both the date from filename and dates from csv's with discounts
//    public void addToLocalDate(LocalDate date) {
//        try {
//            this.date.add(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
