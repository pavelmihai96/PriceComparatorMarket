package code.PriceComparatorMarket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    /// additional members needed
    @JsonIgnore
    private LocalDate date;
    private String store;
}
