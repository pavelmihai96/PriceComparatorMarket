package code.PriceComparatorMarket.services;

import code.PriceComparatorMarket.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
//@AllArgsConstructor
@Component
public class FileChecker {

    @Value("${csv.folder.path}")
    private String path;

    private final Set<String> knownFiles = new HashSet<>();

    @Autowired
    private final ProductRepository productRepository;

    @Scheduled(fixedDelay = 5000) /// milliseconds
    public void checkForNewFiles() {
        File folder = new File(path);
        File[] files = folder.listFiles();

        if (files == null) {
            return;
        }

        Set<String> currentFileNames = new HashSet<>();
        for (File file : files) {
            String fileName = file.getName();
            currentFileNames.add(fileName);

            if (!knownFiles.contains(fileName)) {
                System.out.println("New file: " + fileName);
                checkForNewFilesOnDay();
            }
        }

        knownFiles.clear();
        knownFiles.addAll(currentFileNames);
    }

    @Scheduled(fixedDelay = 86400000) ///  1 day
    public void checkForNewFilesOnDay() {

    }
}
