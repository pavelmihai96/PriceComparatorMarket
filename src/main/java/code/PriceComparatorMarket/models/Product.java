package code.PriceComparatorMarket.models;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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
}
