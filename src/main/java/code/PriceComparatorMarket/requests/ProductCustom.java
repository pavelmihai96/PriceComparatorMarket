package code.PriceComparatorMarket.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductCustom {
    private String productName;
    private String productCategory;
    private String brand;
}
