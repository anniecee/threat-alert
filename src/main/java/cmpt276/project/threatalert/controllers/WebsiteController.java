package cmpt276.project.threatalert.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import cmpt276.project.threatalert.models.Comment;
import cmpt276.project.threatalert.models.CommentRepository;
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

    // When an url is scanned, a website object is added to table and function returns list of comments of that website
    // @PostMapping("/scanning")
    // public void addingWebsite(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session) {
        
    //     String newlink = formData.get("link");
    //     //check if link is already in repository
    //     List<Website> websiteList = websiteRepo.findByLink(newlink);

    //     // if no, create the website object with an empty list of comments
    //     Date newDate = new Date();
    //     if (websiteList.isEmpty()) {
    //         List<Comment> newcomments = new ArrayList<>();
    //         Website website = new Website(newDate, newlink, newcomments);
    //         websiteRepo.save(website);
    //         model.addAttribute("website", website);
    //         return;
    //     }

    //     // else, access and read comments from website with that link
    //     else{
    //         Website existingWebsite = websiteList.get(0);
    //         model.addAttribute("website", existingWebsite);
    //         model.addAttribute("link", existingWebsite.getLink());
    //         return ;
    //     }
    // }


    @PostMapping("/scanning")
    public ResponseEntity<?> addingWebsite(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session) {
    
        String newlink = formData.get("link");
        //check if link is already in repository
        List<Website> websiteList = websiteRepo.findByLink(newlink);

        // if no, create the website object with an empty list of comments
        Date newDate = new Date();
        if (websiteList.isEmpty()) {
            List<Comment> newcomments = new ArrayList<>();
            Website website = new Website(newDate, newlink, newcomments);
            websiteRepo.save(website);
            return ResponseEntity.ok(newcomments);
        }

        // else, access and read comments from website with that link
        else{
            Website existingWebsite = websiteList.get(0);
            List<Comment> commentlist = existingWebsite.getComments();
            return ResponseEntity.ok(commentlist);
        }
    }
}
