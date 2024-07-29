package cmpt276.project.threatalert.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import jakarta.servlet.http.HttpSession;


@Controller
public class WebsiteController {

    @Autowired
    private UserRepository userRepo;
    
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

    @GetMapping({"/home", "/scan"})
    public String home(Model model, HttpSession session) {

        System.out.println("viewing scan page");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }
        
        // user = userRepo.findByUid(user.getUid()).get(0);
        model.addAttribute("user", user);

        return "scan/urlscan";
    }

    
}
