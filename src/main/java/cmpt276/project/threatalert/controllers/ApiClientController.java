package cmpt276.project.threatalert.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cloudmersive.client.ScanApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.model.VirusScanResult;

@RestController
public class ApiClientController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ApiClient apiClient;

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload"; 
    }
    
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        try {
            // Save the uploaded file to a temporary location
            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            // Use the configured ApiClient to create the ScanApi instance
            ScanApi apiInstance = new ScanApi(apiClient);
            File inputFile = path.toFile();
            try {
                VirusScanResult result = apiInstance.scanFile(inputFile);
                return "Scan result: " + result.toString();
            } catch (ApiException e) {
                e.printStackTrace();
                return "Exception when calling ScanApi#scanFile: " + e.getMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
    }
}