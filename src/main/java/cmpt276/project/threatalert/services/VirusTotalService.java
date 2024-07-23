package cmpt276.project.threatalert.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(analysisUrl))
                .header("accept", "application/json")
                .header("x-apikey", apiKey)
                .GET()
                .build();

        logger.info("Sending request to get analysis results...");
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Analysis results received. Status code: {}", response.statusCode());
        logger.info("Analysis results body: {}", response.body());

        return response.body();
    }
}