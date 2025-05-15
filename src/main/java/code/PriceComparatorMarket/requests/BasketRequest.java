package code.PriceComparatorMarket.requests;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BasketRequest {
    private List<String> productIds;
    private LocalDate date;
}
