package cmpt276.project.threatalert.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import cmpt276.project.threatalert.services.VirusTotalService;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

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
            model.addAttribute("result", result);

            // creating Website object and saving to User
            Website website = createWebsite(url, result);

            User user = (User) session.getAttribute("session_user");
            if (user == null) {
                return "redirect:/user/login";
            }
            saveWebsiteToUser(user, website);
            website.setUser(user);

            websiteRepo.save(website);
            userRepo.save(user);

        } catch (IOException | InterruptedException e) {
            logger.error("Error scanning URL: {}", e.getMessage(), e);
            model.addAttribute("result", "Error: " + e.getMessage());
        }

        return "urlscan";
    }

        private Website createWebsite(String url, String result) {
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