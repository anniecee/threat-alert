package cmpt276.project.threatalert.controllers;

import org.springframework.web.bind.annotation.RestController;

import cmpt276.project.threatalert.models.Comment;
import cmpt276.project.threatalert.models.CommentRepository;
import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class CommentController {
    
    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private WebsiteRepository websiteRepo;

    @Autowired
    private UserRepository userRepo;


    @PostMapping("/addcomment")
    public String addComment(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session) {
        
        System.out.println("adding comment");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }

        //Get the most recent scanned website
        Website website = user.getHistory().get(user.getHistory().size()-1);
        website = websiteRepo.findByLink(website.getLink()).get(0);

        String newContent = formData.get("content");
        Date newDate = new Date();

        Comment newComment = new Comment(user, newContent, newDate);

        website.addComment(newComment);
        commentRepo.save(newComment);

        return "website/comment";
    }

    @GetMapping("/viewcomment")
    public String viewComment(Model model, HttpSession session) {

        System.out.println("viewing comment");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }

        //Get the most recent scanned website
        
        System.out.println("viewing comment2");
        //Website website = user.getHistory().get(user.getHistory().size()-1);
        Website website = user.getHistory().get

        System.out.println("viewing comment3");

        List<Comment> comments = website.getComment();
        System.out.println("viewing comment4");
        model.addAttribute("comments", comments);
        System.out.println("viewing comment5");
        return "website/comment";
    }
    
    
}
