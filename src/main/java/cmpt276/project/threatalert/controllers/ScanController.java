package cmpt276.project.threatalert.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/vt")
public class ScanController {

    @Value("5796597306b3edb03cf9aa494b58a755431d95042f093fc3cc46375e52be9687")
    private String apiKey;

    private static final String VT_BASE_URL = "https://www.virustotal.com/api/v3";

    // Helper method to create URL-encoded form data
    private String createUrlEncodedFormData(String scannedUrl) throws UnsupportedEncodingException {
        String encodedUrl = URLEncoder.encode(scannedUrl, "UTF-8");
        return "url=" + encodedUrl;
    }

    @PostMapping("/scan")
    public ResponseEntity<String> sendUrl(@RequestParam String unscannedUrl) {
        System.out.println("scan api");
        String url = VT_BASE_URL + "/urls";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("x-apikey", apiKey);
        headers.set("content-type", "application/x-www-form-urlencoded");

        String body;
        try {
            body = createUrlEncodedFormData(unscannedUrl);
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.badRequest().body("Error encoding URL");
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/analysis")
    public ResponseEntity<String> getAnalysis(@RequestParam String analysisId) {
        System.out.println("analyse api");
        String url = VT_BASE_URL + "/analyses/" + analysisId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("x-apikey", apiKey);


        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> getTestApi() {
        return ResponseEntity.status(200).body("api test");
    }
    
    
}
