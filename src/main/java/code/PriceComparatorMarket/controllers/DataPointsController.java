package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.requests.DataPointsRequest;
import code.PriceComparatorMarket.services.DataPointsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/filter")
public class DataPointsController {
    private final DataPointsService dataPointsService;

    @GetMapping("/by-productId")
    public ResponseEntity<?> filterByProductId(@RequestBody DataPointsRequest request) {
        return dataPointsService.filterByProductId(request);
    }

    @GetMapping("/by-category")
    public ResponseEntity<?> filterByCategory(@RequestBody DataPointsRequest request) {
        return dataPointsService.filterByCategory(request);
    }

    @GetMapping("/by-store")
    public ResponseEntity<?> filterByStore(@RequestBody DataPointsRequest request) {
        return dataPointsService.filterByStore(request);
    }

    @GetMapping("/by-brand")
    public ResponseEntity<?> filterByBrand(@RequestBody DataPointsRequest request) {
        return dataPointsService.filterByBrand(request);
    }
}
