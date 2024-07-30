package cmpt276.project.threatalert.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

import cmpt276.project.threatalert.models.Comment;
import cmpt276.project.threatalert.models.Scan;
import cmpt276.project.threatalert.models.ScanRepository;
import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import cmpt276.project.threatalert.services.VirusTotalService;
import cmpt276.project.threatalert.services.OpenAIService;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@Controller
public class VirusTotalController {

    private static final Logger logger = LoggerFactory.getLogger(VirusTotalController.class);

    @Autowired
    private VirusTotalService virusTotalService;

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private WebsiteRepository websiteRepo;

    @Autowired 
    private UserRepository userRepo;

    @Autowired
    private ScanRepository scanRepo;

    @PostMapping("/scan")
    public String scanUrl(@RequestParam("url") String url, Model model, HttpSession session) {
        logger.info("Received URL for scanning: {}", url);
        try {

            User sessionUser = (User) session.getAttribute("session_user");
            if (sessionUser == null) {
                return "redirect:/user/login";
            }
            // Fetch the user from the repository to ensure it's persisted
            Optional<User> optionalUser = userRepo.findById(sessionUser.getUid());
            if (!optionalUser.isPresent()) {
                model.addAttribute("error", "User not found. Please log in again.");
                return "redirect:/user/login";
            }
            User user = optionalUser.get();
            logger.info("User found: id = {}, email = {}", user.getUid(), user.getEmail());
            model.addAttribute("user", user);
            
            String result = virusTotalService.scanUrl(url);

            List<Website> websites = websiteRepo.findByLink(url);
            Website website;
            if (websites.isEmpty()) {
                logger.info("\nwebsite not in repo");
                // Verify the scan result
                JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
                if (jsonObject.has("error")) {
                    logger.info("invalid url given");
                    model.addAttribute("error", "Error reading URL. Please make sure to enter a valid URL.");
                    return "scan/urlscan";
                }
                //helper function below
                website = createWebsite(url, result);
            }
            else {
                logger.info("\nwebsite in repo");
                website = websites.get(0);
            }
            
            Scan scan = new Scan(website);

            List<Comment> commentList = website.getComments();
            sortByCommentDate(commentList);

            model.addAttribute("comments", commentList);
            websiteRepo.save(website);
            user.addScan(scan);
            scan.setUser(user);
            scanRepo.save(scan);
            userRepo.save(user);
            logger.info("saved to user repo");

            session.setAttribute("scanned_url", url);

            model.addAttribute("website", website);

            model.addAttribute("user", user);



            // Display scan result
            displayResult(result, model);
            model.addAttribute("result", true);

            

        } catch (IOException | InterruptedException e) {
            logger.error("Error scanning URL: {}", e.getMessage(), e);
            model.addAttribute("result", "Error: " + e.getMessage());
        }

        return "scan/urlscan";
    }

    @PostMapping("/filescan")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, HttpSession session) {

        logger.info("Received file for scanning: {}", file);
        try {

            User sessionUser = (User) session.getAttribute("session_user");
            if (sessionUser == null) {
                return "redirect:/user/login";
            }
            // Fetch the user from the repository to ensure it's persisted
            Optional<User> optionalUser = userRepo.findById(sessionUser.getUid());
            if (!optionalUser.isPresent()) {
                model.addAttribute("error", "User not found. Please log in again.");
                return "redirect:/user/login";
            }
            User user = optionalUser.get();
            logger.info("User found: id = {}, email = {}", user.getUid(), user.getEmail());
            model.addAttribute("user", user);

            String fileResult = virusTotalService.scanFile(file);
            displayFileResult(fileResult, model);
            model.addAttribute("result", fileResult);

        } catch (IOException | InterruptedException e) {
            logger.error("Error scanning file: {}", e.getMessage(), e);
            model.addAttribute("result", "Error: " + e.getMessage());
        }

        return "scan/filescan";

    }

    private Website createWebsite(String url, String result) {
        //result is the json string
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonObject("stats");
        int malicious = jsonObject.get("malicious").getAsInt();
        int suspicious = jsonObject.get("suspicious").getAsInt();
        int undetected = jsonObject.get("undetected").getAsInt();
        int harmless = jsonObject.get("harmless").getAsInt();
        int timeout = jsonObject.get("timeout").getAsInt();
        Website website = new Website(url, malicious, suspicious, undetected, harmless, timeout);
        
        return website;
    }

    private void displayFileResult(String scanResultString, Model model) {
        // Create JsonObject from scan result string
        JsonObject result = JsonParser.parseString(scanResultString).getAsJsonObject();
    
        // Get file info
        JsonObject fileInfo = result.getAsJsonObject("meta").getAsJsonObject("file_info");
        String sha256 = fileInfo.getAsJsonPrimitive("sha256").getAsString();
        String md5 = fileInfo.getAsJsonPrimitive("md5").getAsString();
        String sha1 = fileInfo.getAsJsonPrimitive("sha1").getAsString();
        long size = fileInfo.getAsJsonPrimitive("size").getAsLong();
    
        // Get scan status and stats
        JsonObject attributes = result.getAsJsonObject("data").getAsJsonObject("attributes");
        String status = attributes.getAsJsonPrimitive("status").getAsString();
        JsonObject stats = attributes.getAsJsonObject("stats");
        int malicious = stats.getAsJsonPrimitive("malicious").getAsInt();
        int suspicious = stats.getAsJsonPrimitive("suspicious").getAsInt();
        int undetected = stats.getAsJsonPrimitive("undetected").getAsInt();
        int harmless = stats.getAsJsonPrimitive("harmless").getAsInt();
        int timeout = stats.getAsJsonPrimitive("timeout").getAsInt();
    
        // Add file info to model
        model.addAttribute("sha256", sha256);
        model.addAttribute("md5", md5);
        model.addAttribute("sha1", sha1);
        model.addAttribute("size", size);
    
        // Add scan status and stats to model
        model.addAttribute("status", status);
        model.addAttribute("malicious", malicious);
        model.addAttribute("suspicious", suspicious);
        model.addAttribute("undetected", undetected);
        model.addAttribute("harmless", harmless);
        model.addAttribute("timeout", timeout);
    
        // Generate the summary from OpenAIService
        try {
            String summary = openAIService.summarizeFile(scanResultString);
            model.addAttribute("summary", summary);
        } catch (IOException | InterruptedException e) {
            logger.error("Error summarizing scan result: {}", e.getMessage(), e);
            model.addAttribute("summary", "Error summarizing scan result.");
        }
    }
    

    private void displayResult(String scanResultString, Model model) {
        
        // Create JsonObject from scan result string
        JsonObject result = JsonParser.parseString(scanResultString).getAsJsonObject();
        
        // Get security vendors' analysis
        JsonObject vendorObject = result.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonObject("results");
        Set<Map.Entry<String,JsonElement>> vendorEntries = vendorObject.entrySet();

        List<HashMap<String, String>> vendorResults = new ArrayList<>(); // List to save results from vendors

        for (Map.Entry<String, JsonElement> entry : vendorEntries) {
            JsonObject entryObject = entry.getValue().getAsJsonObject();

            String nameString = entryObject.getAsJsonPrimitive("engine_name").getAsString();
            String categoryString = entryObject.getAsJsonPrimitive("category").getAsString();
            String resultString = entryObject.getAsJsonPrimitive("result").getAsString();
            
            HashMap<String, String> entryMap = new HashMap<String, String>();;
            entryMap.put("engine_name", nameString);
            entryMap.put("category", categoryString);
            entryMap.put("result", resultString);

            vendorResults.add(entryMap);
        }
        model.addAttribute("vendorResults", vendorResults);

        // Generate the summary from OpenAIService
        try {
            String summary = openAIService.summarizeURL(scanResultString);
            model.addAttribute("summary", summary);
        } catch (IOException | InterruptedException e) {
            logger.error("Error summarizing scan result: {}", e.getMessage(), e);
            model.addAttribute("summary", "Error summarizing scan result.");
        }
    }

        // Method to sort comments by date in descending order
        private void sortByCommentDate(List<Comment> comments) {
            Collections.sort(comments, new Comparator<Comment>() {
                @Override
                public int compare(Comment c1, Comment c2) {
                    return c2.getDate().compareTo(c1.getDate()); // Descending order
                }
            });

    }
}