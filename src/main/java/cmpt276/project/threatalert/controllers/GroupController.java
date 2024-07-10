package cmpt276.project.threatalert.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import cmpt276.project.threatalert.models.GroupRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GroupController {
    
    @Autowired
    private GroupRepository groupRepo;
    
    @GetMapping("/group/display")
    public String getGroupDisplay(Model model, HttpServletRequest request, HttpSession session) {
        
        return new String();
    }
    
}
