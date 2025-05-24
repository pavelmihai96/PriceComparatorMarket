package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.repositories.ProductRepository;
import code.PriceComparatorMarket.requests.PriceAlertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@RequiredArgsConstructor
@Component
public class FileChecker {
    @Value("${csv.folder.path}")
    private String path;

    private final HashMap<String, Long> knownFilesWithCreation = new HashMap<>();

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final BasketService basketService;

    /// 1. A Map of all files is stored in the knownFilesWithCreation member of the class, with a pair of <filename, creationTime>
    /// 2. Every 5 seconds checkForNewFiles method is called. The first time is called is after the application runs
    ///    Then it gets called every 5 seconds and checks if a new file is added to the folder, or if any of the present files has changed
    /// 3. If it is the case, then checkForNewFilesOnDay get called, which verifies the alerts set by the user
    /// 4. Alerts sent by the user are saved in price-alert.csv from data folder, and they are retrieved with productRepository.loadPriceAlerts()
    @Scheduled(fixedDelay = 5000) /// 5 seconds
    public void checkForNewFiles() throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files == null) {
            return;
        }

        HashMap<String, Long> currentFileNamesWithCreation = new HashMap<>();
        for (File file : files) {

            String fileName = file.getName();
            Long fileCreated = Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toMillis();
            currentFileNamesWithCreation.put(fileName, fileCreated);

            if (!knownFilesWithCreation.containsKey(fileName) || (knownFilesWithCreation.containsKey(fileName) && (!Objects.equals(currentFileNamesWithCreation.get(fileName), knownFilesWithCreation.get(fileName))))) {
                System.out.println("New file: " + fileName);
                checkForNewFilesOnDay();
            }
        }

        knownFilesWithCreation.clear();
        knownFilesWithCreation.putAll(currentFileNamesWithCreation);
    }

    /// checkForNewFilesOnDay method gets called every day once,
    /// and checks if the prices are now lower or equal to the ones saved in price-alert.csv
    @Scheduled(fixedDelay = 86400000) ///  1 day
    public void checkForNewFilesOnDay() {
        try {
            /// loads the price alerts saved
            List<PriceAlertRequest> listPA = productRepository.loadPriceAlerts();

            /// calls the service method which is also called when the frontend request is made
            basketService.priceAlert(listPA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
