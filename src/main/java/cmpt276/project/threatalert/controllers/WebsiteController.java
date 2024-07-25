package cmpt276.project.threatalert.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class WebsiteController {
    
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
        return "scan/urlscan";
    }
    
}
