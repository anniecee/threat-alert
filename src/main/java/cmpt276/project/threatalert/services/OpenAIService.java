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

    private String openaiApiKey = "sk-proj-k4b3UQPUD8BF7KW7irsOT3BlbkFJ5FSrxToIchPN0PSfkZ8H";

    private static final Logger logger = Logger.getLogger(OpenAIService.class.getName());

    public String summarize(String text) throws IOException, InterruptedException {
        String apiUrl = "https://api.openai.com/v1/chat/completions";
        HttpClient client = HttpClient.newHttpClient();

        JsonObject prompt = new JsonObject();
        prompt.addProperty("model", "gpt-4o-mini-2024-07-18");
        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", "Summarize the following information from VirusTotal's analysis of a URL into a brief paragraph.\n" + //
                        "\n" + //
                        "Results: A dictionary having the engine's name as key and its result as value. Each result contains:\n" + //
                        "Category: <string> normalized result. Possible values are:\n" + //
                        "\"confirmed-timeout\" (AV reached a timeout when analyzing that file)\n" + //
                        "\"failure\" (AV failed when analyzing this file)\n" + //
                        "\"harmless\" (AV thinks the file is not malicious)\n" + //
                        "\"undetected\" (AV has no opinion about this file)\n" + //
                        "\"suspicious\" (AV thinks the file is suspicious)\n" + //
                        "\"malicious\" (AV thinks the file is malicious)\n" + //
                        "\"type-unsupported\" (AV can't analyze that file)\n" + //
                        "Engine Name: <string> the engine's name.\n" + //
                        "Engine Update: <string> the engine's update date in %Y%M%D format.\n" + //
                        "Engine Version: <string> the engine's version.\n" + //
                        "Method: <string> detection method.\n" + //
                        "Result: <string> engine result.\n" + //
                        "Stats: Summary of the results field, containing:\n" + //
                        "Confirmed-timeout: <integer> number of AV engines that reach a timeout.\n" + //
                        "Failure: <integer> number of AV engines that fail.\n" + //
                        "Harmless: <integer> number of reports saying the URL is harmless.\n" + //
                        "Malicious: <integer> number of reports saying the URL is malicious.\n" + //
                        "Suspicious: <integer> number of reports saying the URL is suspicious.\n" + //
                        "Timeout: <integer> number of timeouts.\n" + //
                        "Type-unsupported: <integer> number of AV engines that don't support that type of file.\n" + //
                        "Undetected: <integer> number of reports saying the URL is undetected.\n" + //
                        "Status: <string> analysis status. Possible values are:\n" + //
                        "\"completed\" (the analysis is finished)\n" + //
                        "\"queued\" (the item is waiting to be analyzed)\n" + //
                        "\"in-progress\" (the file is being analyzed)\n" + //

                        "Example Analysis Object:\n" + //
                        "{\n" + //
                        "\"results\": {\n" + //
                        "\"Engine1\": {\"category\": \"harmless\", \"engine_name\": \"Engine1\", \"method\": \"blacklist\", \"result\": \"clean\"},\n" + //
                        "\"Engine2\": {\"category\": \"malicious\", \"engine_name\": \"Engine2\", \"method\": \"heuristic\", \"result\": \"phishing\"}\n" + //
                        "},\n" + //
                        "\"stats\": {\n" + //
                        "\"confirmed-timeout\": 0,\n" + //
                        "\"failure\": 0,\n" + //
                        "\"harmless\": 70,\n" + //
                        "\"malicious\": 1,\n" + //
                        "\"suspicious\": 0,\n" + //
                        "\"timeout\": 0,\n" + //
                        "\"type-unsupported\": 0,\n" + //
                        "\"undetected\": 24\n" + //
                        "},\n" + //
                        "\"status\": \"completed\"\n" + //
                        "}\n" + //
                        "Suggest any steps the user can take in ensuring their cyber safetey when entering this URL." + text);
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
        logger.info("OpenAI API response body: " + response.body());

        JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices != null && choices.size() > 0) {
            JsonObject choice = choices.get(0).getAsJsonObject();
            JsonObject messageObject = choice.getAsJsonObject("message");
            if (messageObject != null && messageObject.has("content")) {
                String content = messageObject.get("content").getAsString();
                String rawText = content.replaceAll("\\n", " ").replaceAll("\\r", " ").trim();

                // logger.info("Content field: " + content);
                // logger.info("Raw text: " + rawText);
                return rawText;
            }
        }

        logger.warning("Unexpected JSON structure in OpenAI API response: " + jsonResponse);
        return "No summary available";
    }
}