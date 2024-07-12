// package cmpt276.project.threatalert.models;

// import org.apache.hc.client5.http.HttpResponseException;
// import org.apache.hc.client5.http.classic.methods.HttpPost;
// import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
// import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
// import org.apache.hc.client5.http.impl.classic.HttpClients;
// import org.apache.hc.core5.http.io.entity.StringEntity;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.charset.StandardCharsets;
// import org.apache.commons.io.IOUtils;
// import java.util.logging.Logger;

// @Service
// public class VirusTotal {

//     private static final Logger logger = Logger.getLogger(VirusTotal.class.getName());

//     @Value("${virustotal.api.key}")
//     private String apiKey;

//     private static final String URL_SCAN_ENDPOINT = "https://www.virustotal.com/api/v3/urls";

//     public String scanUrl(String url) throws IOException {
//         logger.info("Scanning URL: " + url);
//         try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//             HttpPost request = new HttpPost(URL_SCAN_ENDPOINT);
//             request.addHeader("x-apikey", apiKey);
//             request.addHeader("Content-Type", "application/json");

//             String jsonPayload = String.format("{\"url\":\"%s\"}", url);
//             logger.info("Payload: " + jsonPayload);
//             request.setEntity(new StringEntity(jsonPayload));

//             try (CloseableHttpResponse response = httpClient.execute(request)) {
//                 int statusCode = response.getCode();
//                 String reasonPhrase = response.getReasonPhrase();
//                 logger.info("Response status: " + statusCode + " - " + reasonPhrase);

//                 if (statusCode != 200) {
//                     throw new HttpResponseException(statusCode, reasonPhrase);
//                 }

//                 InputStream responseStream = response.getEntity().getContent();
//                 String responseString = IOUtils.toString(responseStream, StandardCharsets.UTF_8);
//                 logger.info("Response: " + responseString);

//                 return responseString;
//             }
//         }
//     }
// }