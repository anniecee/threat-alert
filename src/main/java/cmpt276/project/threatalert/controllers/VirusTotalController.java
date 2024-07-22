package cmpt276.project.threatalert.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.threatalert.services.VirusTotalService;

import java.io.IOException;

@Controller
public class VirusTotalController {

    private static final Logger logger = LoggerFactory.getLogger(VirusTotalController.class);

    @Autowired
    private VirusTotalService virusTotalService;

    @PostMapping("/scan")
    public String scanUrl(@RequestParam("url") String url, Model model) {
        logger.info("Received URL for scanning: {}", url);
        try {
            String result = virusTotalService.scanUrl(url);
            model.addAttribute("result", result);
        } catch (IOException | InterruptedException e) {
            logger.error("Error scanning URL: {}", e.getMessage(), e);
            model.addAttribute("result", "Error: " + e.getMessage());
        }

        return "urlscan";
    }
}