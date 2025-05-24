package code.PriceComparatorMarket.requests;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DataPointsRequest {
    private String productId;
    private String category;
    private String brand;
    private String store;
    private LocalDate startDate;
    private LocalDate endDate;
}
