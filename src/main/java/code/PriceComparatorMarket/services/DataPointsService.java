package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.models.Product;
import code.PriceComparatorMarket.models.ProductDiscount;
import code.PriceComparatorMarket.repositories.CsvRepository;
import code.PriceComparatorMarket.requests.DataPointsRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class DataPointsService {
    @Autowired
    private final ProductService productService;

    @Autowired
    private final CsvRepository<Product> productRepository;

    @Autowired
    private final ProductDiscountService productDiscountService;

    public ResponseEntity<?> filterByCategory(DataPointsRequest request, String category) {
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
        ///           { "lapte" : 10 RON,
        ///             "oua" : 5 RON,
        ///           }
        ///           ...
        ///       }
        ///       ...
        ///   }
        Map<LocalDate, Map<String, HashMap<String, Double>>> returnedData = new HashMap<>();


        /// 1. First is a loop over all the dates, and getting all products from the repository with each date
        /// 2. For a specific date, products will be filtered based on the filter from the parameters and saved into a list of products
        /// 3. Then, if applicable, the discount will be applied
        /// 4. Then, in the returnedData Map, will be put the current looped date as a key
        /// 5. Then the inside Map will be handled (details below)

        allDates.forEach(d -> {
            /// pentru un LocalDate, ex: 2025-05-01 -> sa caute toate produsele din fisierele din data aceea,
            /// voi avea un List<Product> -> apoi se vor filtra doar acelea care apartin categoriei
            /// voi avea un List<Product> al categoriei
            /// apoi pentru fiecare se va aplica discount, daca se poate
            /// apoi grupez in functie de magazin
            List<Product> listPD = productRepository.loadProductsByDate(d).stream()
                    .filter(p -> p.getProductCategory().equalsIgnoreCase(category))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            /// put the current date as a key if it's not already there
            returnedData.putIfAbsent(d, new HashMap<>());

            /// extract the inside Map of the current date
            /// this works and you can handle it separately because the same address from memory is kept,
            /// even if the value of returnedData.get(d) is stored in another variable
            Map<String, HashMap<String, Double>> storeMap = returnedData.get(d);

            listPD.forEach(p -> {
                storeMap.computeIfAbsent(p.getStore(), k -> new HashMap<>());

                HashMap<String, Double> brandNameMap = storeMap.get(p.getStore());

                brandNameMap.put(p.getProductName(), p.getPrice());
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
    public ResponseEntity<?> filterByStore(DataPointsRequest request, String store) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Start date is after the end date."));
        }

        Stream<LocalDate> allDates = request.getStartDate().datesUntil(request.getEndDate().plusDays(1));
        Map<LocalDate, Map<String, HashMap<String, Double>>> returnedData = new HashMap<>();

        allDates.forEach(d -> {
            List<Product> listPD = productRepository.loadProductsByDate(d).stream()
                    .filter(p -> p.getStore().equalsIgnoreCase(store))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            returnedData.putIfAbsent(d, new HashMap<>());

            Map<String, HashMap<String, Double>> categoryMap = returnedData.get(d);

            listPD.forEach(p -> {
                categoryMap.computeIfAbsent(p.getProductCategory(), k -> new HashMap<>());

                HashMap<String, Double> brandNameMap = categoryMap.get(p.getProductCategory());

                brandNameMap.put(p.getProductName(), p.getPrice());
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
    public ResponseEntity<?> filterByBrand(DataPointsRequest request, String brand) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Start date is after the end date."));
        }

        Stream<LocalDate> allDates = request.getStartDate().datesUntil(request.getEndDate().plusDays(1));
        Map<LocalDate, Map<String, HashMap<String, Double>>> returnedData = new HashMap<>();

        allDates.forEach(d -> {
            List<Product> listPD = productRepository.loadProductsByDate(d).stream()
                    .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                    .peek(p -> System.out.println("ID: " + p.getProductId() + ", " + p.getProductCategory()))
                    .peek(p -> productDiscountService.applyDiscount(p, d))
                    .toList();

            returnedData.putIfAbsent(d, new HashMap<>());

            Map<String, HashMap<String, Double>> storeMap = returnedData.get(d);

            listPD.forEach(p -> {
                storeMap.computeIfAbsent(p.getStore(), k -> new HashMap<>());

                HashMap<String, Double> brandNameMap = storeMap.get(p.getStore());

                brandNameMap.put(p.getProductName(), p.getPrice());
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
