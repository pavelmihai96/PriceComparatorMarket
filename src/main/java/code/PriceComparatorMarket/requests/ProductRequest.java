package code.PriceComparatorMarket.requests;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductRequest {
    private List<ProductCustom> products;
    private LocalDate date;
}
