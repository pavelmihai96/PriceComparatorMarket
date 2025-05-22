package code.PriceComparatorMarket.controllers;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.requests.BasketRequest;
import code.PriceComparatorMarket.requests.DataPointsRequest;
import code.PriceComparatorMarket.services.BasketService;
import code.PriceComparatorMarket.services.DataPointsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("api/filter")
public class DataPointsController {

    private final DataPointsService dataPointsService;

    @PostMapping("/by-productId/{productId}")
    public ResponseEntity<?> filterByProductId(@RequestBody DataPointsRequest request, @PathVariable String productId) {
        return dataPointsService.filterByProductId(request, productId);
    }

    @PostMapping("/by-category/{category}")
    public ResponseEntity<?> filterByCategory(@RequestBody DataPointsRequest request, @PathVariable String category) {
        return dataPointsService.filterByCategory(request, category);
    }

    @PostMapping("/by-store/{store}")
    public ResponseEntity<?> filterByStore(@RequestBody DataPointsRequest request, @PathVariable String store) {
        return dataPointsService.filterByStore(request, store);
    }

    @PostMapping("/by-brand/{brand}")
    public ResponseEntity<?> filterByBrand(@RequestBody DataPointsRequest request, @PathVariable String brand) {
        return dataPointsService.filterByBrand(request, brand);
    }
}
