package cmpt276.project.threatalert.controllers;

import org.springframework.web.bind.annotation.RestController;

import cmpt276.project.threatalert.models.Comment;
import cmpt276.project.threatalert.models.CommentRepository;
import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    
    @PostMapping("/addComment")
    public String signup(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        //TODO: process POST request
        
        return entity;
    }
    


}