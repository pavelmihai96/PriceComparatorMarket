package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.repositories.CsvRepository;
import code.PriceComparatorMarket.requests.DataPointsRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class DataPointsService {
    @Autowired
    private final CsvRepository<Product> productRepository;

    @Autowired
    private final ProductDiscountService productDiscountService;

    public ResponseEntity<?> filterByProductId(DataPointsRequest request) {

        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Start date is after the end date."));
        }

        /// A stream object of dates in range of dates coming from the request
        Stream<LocalDate> allDates = request.getStartDate().datesUntil(request.getEndDate().plusDays(1));


        ///   returnedData would look like this ->
        ///   { "2025-05-01" :
        ///       { "lidl" :
        ///           { "lapte" : 10 RON
        ///           }
        ///           ...
        ///       }
        ///       ...
        ///   }
        Map<LocalDate, Map<String, List<Product>>> returnedData = new HashMap<>();


        /// 1. First is a loop over all the dates, and getting all products from the repository with each date
        /// 2. For a specific date, products will be filtered based on the filter from the parameters and saved into a list of products
        /// 3. Then, if applicable, the discount will be applied
        /// 4. Then, in the returnedData Map, will be put the current looped date as a key
        /// 5. Then the inside Map will be handled (details below)
        allDates.forEach(d -> {
            List<Product> listP = productRepository.loadAllProducts().stream()
                    .filter(p -> p.getProductId().equalsIgnoreCase(request.getProductId()))
                    .filter(p -> !p.getDate().isAfter(d) && !p.getDate().isBefore(d.minusDays(6)))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            /// put the current date as a key if it's not already there
            returnedData.putIfAbsent(d, new HashMap<>());

            /// extract the inside Map of the current date
            /// this works and you can handle it separately because the same address from memory is kept,
            /// even if the value of returnedData.get(d) is stored in another variable
            Map<String, List<Product>> storeMap = returnedData.get(d);

            listP.forEach(p -> {
                storeMap.computeIfAbsent(p.getStore(), k -> new ArrayList<>());
                List<Product> brandNameMap = storeMap.get(p.getStore());
                brandNameMap.add(new Product(p.getProductId(), p.getProductName(), p.getPackageQuantity(), p.getPackageUnit(), p.getPrice()));
            });
        });

        if (returnedData.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No product info is available for this period and productId."));
        } else {
            return ResponseEntity.ok().body(returnedData);
        }
    }

    /// The same reasoning from 'filterByCategory' method is applied here as well
    /// The only difference is made by the filter coming from the second parameter of the method,
    /// and the way the data is rendered to frontend afterward
    public ResponseEntity<?> filterByCategory(DataPointsRequest request) {

        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Start date is after the end date."));
        }

        Stream<LocalDate> allDates = request.getStartDate().datesUntil(request.getEndDate().plusDays(1));
        Map<LocalDate, Map<String, List<Product>>> returnedData = new HashMap<>();

        allDates.forEach(d -> {
            List<Product> listP = productRepository.loadAllProducts().stream()
                    .filter(p -> p.getProductCategory().equalsIgnoreCase(request.getCategory()))
                    .filter(p -> !p.getDate().isAfter(d) && !p.getDate().isBefore(d.minusDays(6)))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            returnedData.putIfAbsent(d, new HashMap<>());
            Map<String, List<Product>> storeMap = returnedData.get(d);

            listP.forEach(p -> {
                storeMap.computeIfAbsent(p.getStore(), k -> new ArrayList<>());
                List<Product> brandNameMap = storeMap.get(p.getStore());
                brandNameMap.add(new Product(p.getProductId(), p.getProductName(), p.getPackageQuantity(), p.getPackageUnit(), p.getPrice()));
            });
        });

        if (returnedData.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No product info is available for this period and category."));
        } else {
            return ResponseEntity.ok().body(returnedData);
        }
    }

    /// The same reasoning from 'filterByCategory' method is applied here as well
    /// The only difference is made by the filter coming from the second parameter of the method,
    /// and the way the data is rendered to frontend afterward
    public ResponseEntity<?> filterByStore(DataPointsRequest request) {

        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Start date is after the end date."));
        }

        Stream<LocalDate> allDates = request.getStartDate().datesUntil(request.getEndDate().plusDays(1));
        Map<LocalDate, Map<String, List<Product>>> returnedData = new HashMap<>();

        allDates.forEach(d -> {
            List<Product> listP = productRepository.loadAllProducts().stream()
                    .filter(p -> p.getStore().equalsIgnoreCase(request.getStore()))
                    .filter(p -> !p.getDate().isAfter(d) && !p.getDate().isBefore(d.minusDays(6)))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            returnedData.putIfAbsent(d, new HashMap<>());
            Map<String, List<Product>> categoryMap = returnedData.get(d);

            listP.forEach(p -> {
                categoryMap.computeIfAbsent(p.getProductCategory(), k -> new ArrayList<>());
                List<Product> brandNameMap = categoryMap.get(p.getProductCategory());
                brandNameMap.add(new Product(p.getProductId(), p.getProductName(), p.getPackageQuantity(), p.getPackageUnit(), p.getPrice()));
            });
        });

        if (returnedData.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No product info is available for this period and store."));
        } else {
            return ResponseEntity.ok().body(returnedData);
        }
    }

    /// The same reasoning from 'filterByCategory' method is applied here as well
    /// The only difference is made by the filter coming from the second parameter of the method,
    /// and the way the data is rendered to frontend afterward
    public ResponseEntity<?> filterByBrand(DataPointsRequest request) {

        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Start date is after the end date."));
        }

        Stream<LocalDate> allDates = request.getStartDate().datesUntil(request.getEndDate().plusDays(1));
        Map<LocalDate, Map<String, List<Product>>> returnedData = new HashMap<>();

        allDates.forEach(d -> {
            List<Product> listP = productRepository.loadAllProducts().stream()
                    .filter(p -> p.getBrand().equalsIgnoreCase(request.getBrand()))
                    .filter(p -> !p.getDate().isAfter(d) && !p.getDate().isBefore(d.minusDays(6)))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            returnedData.putIfAbsent(d, new HashMap<>());
            Map<String, List<Product>> storeMap = returnedData.get(d);

            listP.forEach(p -> {
                storeMap.computeIfAbsent(p.getStore(), k -> new ArrayList<>());
                List<Product> brandNameMap = storeMap.get(p.getStore());
                brandNameMap.add(new Product(p.getProductId(), p.getProductName(), p.getPackageQuantity(), p.getPackageUnit(), p.getPrice()));
            });
        });

        if (returnedData.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No product info is available for this period and brand."));
        } else {
            return ResponseEntity.ok().body(returnedData);
        }
    }
}
