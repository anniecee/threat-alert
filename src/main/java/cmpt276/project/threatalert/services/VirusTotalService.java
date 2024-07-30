package cmpt276.project.threatalert.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VirusTotalService {

    private static final Logger logger = LoggerFactory.getLogger(VirusTotalService.class);

    private final String apiKey = "aea70deb0a18f90e41eb99a5e380dcb8ab19d1af022a9c032b8cf0bc3ab7f2b0";
    private final String apiUrl = "https://www.virustotal.com/api/v3/urls";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String scanUrl(String url) throws IOException, InterruptedException {
        String encodedUrl = "url=" + URLEncoder.encode(url, StandardCharsets.UTF_8);
        logger.info("Encoded URL: {}", encodedUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("accept", "application/json")
                .header("x-apikey", apiKey)
                .header("content-type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(encodedUrl))
                .build();

        logger.info("Sending request to VirusTotal...");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Response received. Status code: {}", response.statusCode());
        
        logger.info("Response body: {}", response.body());

        // Parse the response to get the analysis ID
        JsonNode jsonNode = objectMapper.readTree(response.body());
        String analysisId = jsonNode.path("data").path("id").asText();
        logger.info("Analysis ID: {}", analysisId);

        // Use the analysis ID to get the analysis results
        return getAnalysisResults(analysisId);
    }

    private String getAnalysisResults(String analysisId) throws IOException, InterruptedException {
        String analysisUrl = "https://www.virustotal.com/api/v3/analyses/" + analysisId;

        while(true) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(analysisUrl))
                    .header("accept", "application/json")
                    .header("x-apikey", apiKey)
                    .GET()
                    .build();

            logger.info("Sending request to get analysis results...");
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Analysis results received. Status code: {}", response.statusCode());

            // Parse the response body to check the status
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode statusNode = rootNode.path("data").path("attributes").path("status");

            String status = statusNode.asText();
            if ("completed".equalsIgnoreCase(status)) {
                logger.info("Analysis completed. Results: {}", response.body());
                return response.body();
            } else {
                logger.info("Analysis status: {}. Waiting for completion...", status);
                Thread.sleep(3000); // Wait for a second before checking again
            }

        }

    }

    public String scanFile(MultipartFile file) throws IOException, InterruptedException {
        Path tempFile = Files.createTempFile(null, null);
        file.transferTo(tempFile.toFile());

        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String fileContent = new String(Files.readAllBytes(tempFile));
        String body = "--" + boundary + "\r\n" +
                      "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n" +
                      "Content-Type: " + file.getContentType() + "\r\n\r\n" +
                      fileContent + "\r\n" +
                      "--" + boundary + "--";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.virustotal.com/api/v3/files"))
                .header("accept", "application/json")
                .header("x-apikey", apiKey)
                .header("content-type", "multipart/form-data; boundary=" + boundary)
                .method("POST", HttpRequest.BodyPublishers.ofString(body))
                .build();

        logger.info("Sending request to VirusTotal...");
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Response received. Status code: {}", response.statusCode());
        logger.info("Response body: {}", response.body());

        // Parse the response to get the analysis ID
        JsonNode jsonNode = objectMapper.readTree(response.body());
        String analysisId = jsonNode.path("data").path("id").asText();
        logger.info("Analysis ID: {}", analysisId);
        
        return getAnalysisResults(analysisId);

    }

}