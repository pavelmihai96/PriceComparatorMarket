package code.PriceComparatorMarket.requests;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequest {
    private String productName;
    private String productCategory;
    private String brand;
    private LocalDate date;
}
