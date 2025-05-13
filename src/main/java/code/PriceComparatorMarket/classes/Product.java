package code.PriceComparatorMarket.classes;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
    private String product_id;
    private String product_name;
    private String product_category;
    private String brand;
    private Double package_quantity;
    private String package_unit;
    private Double price;
    private String currency;
}
