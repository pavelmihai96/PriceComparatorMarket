package code.PriceComparatorMarket.requests;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DiscountRequest {
    private LocalDate date;
    private Double hours;
}
