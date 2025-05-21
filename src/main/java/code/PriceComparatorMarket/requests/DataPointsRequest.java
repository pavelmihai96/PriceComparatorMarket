package code.PriceComparatorMarket.requests;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DataPointsRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
