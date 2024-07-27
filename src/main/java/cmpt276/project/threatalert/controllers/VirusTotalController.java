package cmpt276.project.threatalert.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;

import cmpt276.project.threatalert.models.Comment;
import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import cmpt276.project.threatalert.services.VirusTotalService;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;

@Controller
public class VirusTotalController {

    private static final Logger logger = LoggerFactory.getLogger(VirusTotalController.class);

    @Autowired
    private VirusTotalService virusTotalService;

    @Autowired
    private WebsiteRepository websiteRepo;

    @Autowired UserRepository userRepo;

    @PostMapping("/scan")
    public String scanUrl(@RequestParam("url") String url, Model model, HttpSession session) {
        logger.info("Received URL for scanning: {}", url);
        try {
            String result = virusTotalService.scanUrl(url);
            
            // Verify the scan result
            JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            if (jsonObject.has("error")) {
                model.addAttribute("error", "Error reading URL. Please make sure to enter a valid URL.");
                return "scan/urlscan";
            }

            model.addAttribute("result", true);

            // //ADDED TO VIEW COMMENTS

            User user = (User) session.getAttribute("session_user");
            if (user == null) {
                return "redirect:/user/login";
            }
            
            // List<Website> websiteList = websiteRepo.findByLink(url);
            // if (websiteList.isEmpty()){
            //     Website website = createWebsite(url, result);
            //     List<Comment> commentlist = website.getComments();
            //     model.addAttribute("commentlist", commentlist);

            //     saveWebsiteToUser(user, website);
            //     website.setUser(user);
    
            //     websiteRepo.save(website);
            //     userRepo.save(user);
    
            //     model.addAttribute("website", website);
                
            // } else{
            //     Website website = websiteList.get(0);
            //     List<Comment> commentlist = website.getComments();
            //     model.addAttribute("commentlist", commentlist);

            //     saveWebsiteToUser(user, website);
            //     website.setUser(user);
    
            //     websiteRepo.save(website);
            //     userRepo.save(user);
    
            //     model.addAttribute("website", website);
            // }
            



            //ADDING TO HISTORY

            //creating Website object and saving to User
            Website website = createWebsite(url, result);
            User user = (User) session.getAttribute("session_user");
            if (user == null) {
                return "redirect:/user/login";
            }

            saveWebsiteToUser(user, website);
            website.setUser(user);

            websiteRepo.save(website);
            userRepo.save(user);

            model.addAttribute("website", website);

            // Display scan result
            displayResult(result, model);

        } catch (IOException | InterruptedException e) {
            logger.error("Error scanning URL: {}", e.getMessage(), e);
            model.addAttribute("result", "Error: " + e.getMessage());
        }

        return "scan/urlscan";
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

    private void saveWebsiteToUser(User user, Website website) {
        user = userRepo.findByUid(user.getUid()).get(0);
        user.addHistory(website);
    }

}