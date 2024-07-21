package cmpt276.project.threatalert.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class WebsiteController {

    @Autowired 
    private WebsiteRepository websiteRepo;
    
    @GetMapping("/landing")
    public String landing() {

        return "website/landing";

    }

    @GetMapping("/")
    public RedirectView process() {

        return new RedirectView("landing");

    }

    @GetMapping("/back")
    public String back() {

        return "redirect:/landing";

    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/scan.html";
    }

    // When an url is scanned, a website object is added to table
    @PostMapping("/scanning")
    public void addingWebsite(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session) {
        
        String newlink = formData.get("link");

        //check if link is already in repository
        List<Website> websiteList = websiteRepo.findByLink(newlink);

        //check if email is already in repository
        List<User> userList = userRepo.findByEmail(email);

        
        return;
    }
    
    
}
