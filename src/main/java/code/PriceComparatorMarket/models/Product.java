package code.PriceComparatorMarket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private Double packageQuantity;
    private String packageUnit;
    private Double price;
    private String currency;

    /// custom constructor
    public Product (String productId, String productName, Double packageQuantity, String packageUnit, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
        this.price = price;
    }

    /// additional members needed
    //@JsonIgnore
    private LocalDate date;
    @JsonIgnore
    private String store;
    private String valuePerUnit;
}
