package code.PriceComparatorMarket.requests;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PriceAlertRequest {
    private String productId;
    private Double priceAlert;
}
