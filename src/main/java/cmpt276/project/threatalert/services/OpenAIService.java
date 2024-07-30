package cmpt276.project.threatalert.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    private static final Logger logger = Logger.getLogger(OpenAIService.class.getName());

    public String summarizeURL(String text) throws IOException, InterruptedException {
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        HttpClient client = HttpClient.newHttpClient();

        JsonObject prompt = new JsonObject();
        prompt.addProperty("model", "gpt-4o-mini-2024-07-18");
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", "If possible, summarize the contents of the website and what a user can do on it. Then, summarize the following Analysis object to help a " + //
                                                "security-conscious individual understand any dangers a URL may pose. Also suggest any steps the user can take in ensuring their cyber safetey when entering this URL. Put all of this into one single paragraph. " + //
                                                "An Analysis object contains attributes of an analysis of a URL or file submitted to VirusTotal and all their partnered contributors and security vendors.\n\n" + text);
        messages.add(message);
        prompt.add("messages", messages);
        prompt.addProperty("temperature", 1);
        prompt.addProperty("max_tokens", 500);
        prompt.addProperty("top_p", 1);
        prompt.addProperty("frequency_penalty", 0);
        prompt.addProperty("presence_penalty", 0);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openaiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(prompt.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Log the raw response
        logger.info("OpenAI API response body: \n" + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonArray choices = jsonResponse.getAsJsonArray("choices");
if (choices != null && choices.size() > 0) {
    JsonObject choice = choices.get(0).getAsJsonObject();
    JsonObject messageObject = choice.getAsJsonObject("message");
    if (messageObject != null && messageObject.has("content")) {
        String content = messageObject.get("content").getAsString();
        String rawText = content.replaceAll("\\n", " ")
                                .replaceAll("\\r", " ")
                                .replaceAll("\\*\\*", "")
                                .replaceAll("\\*\\*", "")
                                .replaceAll("#", "")
                                .trim();

        // logger.info("Content field: " + content);
        // logger.info("Raw text: " + rawText);
        return rawText;
    }
}

        logger.warning("Unexpected JSON structure in OpenAI API response: " + jsonResponse);
        return "No summary available";
    }

    public String summarizeFile(String text) throws IOException, InterruptedException {
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        HttpClient client = HttpClient.newHttpClient();

        JsonObject prompt = new JsonObject();
        prompt.addProperty("model", "gpt-4o-mini-2024-07-18");
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", "Summarize the following Analysis object to help a security-conscious individual understand any dangers the following file may pose. " + //
                                                "Also suggest any steps the user can take in ensuring their safetey when opening this file. Put all of this into one single paragraph. " + //
                                                "An Analysis object contains attributes of an analysis of a file submitted to VirusTotal and checked with all their partnered contributors and security vendors.\n\n" + text);
        messages.add(message);
        prompt.add("messages", messages);
        prompt.addProperty("temperature", 1);
        prompt.addProperty("max_tokens", 500);
        prompt.addProperty("top_p", 1);
        prompt.addProperty("frequency_penalty", 0);
        prompt.addProperty("presence_penalty", 0);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openaiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(prompt.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Log the raw response
        logger.info("OpenAI API response body: \n" + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonArray choices = jsonResponse.getAsJsonArray("choices");
if (choices != null && choices.size() > 0) {
    JsonObject choice = choices.get(0).getAsJsonObject();
    JsonObject messageObject = choice.getAsJsonObject("message");
    if (messageObject != null && messageObject.has("content")) {
        String content = messageObject.get("content").getAsString();
        String rawText = content.replaceAll("\\n", " ")
                                .replaceAll("\\r", " ")
                                .replaceAll("\\*\\*", "")
                                .replaceAll("\\*\\*", "")
                                .replaceAll("#", "")
                                .trim();

        // logger.info("Content field: " + content);
        // logger.info("Raw text: " + rawText);
        return rawText;
    }
}

        logger.warning("Unexpected JSON structure in OpenAI API response: " + jsonResponse);
        return "No summary available";
    }
}