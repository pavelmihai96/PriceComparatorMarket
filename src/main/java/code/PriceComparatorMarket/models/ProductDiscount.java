package code.PriceComparatorMarket.models;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDiscount {
    private String productId;
    private String productName;
    private String brand;
    private Double packageQuantity;
    private String packageUnit;
    private String productCategory;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Double percentageOfDiscount;
    private String store;
}
