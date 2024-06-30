package cmpt276.project.threatalert.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/user/login")
    public String getLogin(Model model, HttpServletRequest request, HttpSession session) {

        User user = (User)session.getAttribute("session_user");
        if (user == null) {
            
            return "user/login";

        } else {

            model.addAttribute("user", user);
            return "user/protected";

        }

    }

    @PostMapping("/user/login")
    public String login(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session) {

        String email = formData.get("email");
        String password = formData.get("password");

        List<User> userList = userRepo.findByEmailAndPassword(email, password);

        if (userList.isEmpty()) {
            
            return "user/login";

        } else {

            User user = userList.get(0);
            request.getSession().setAttribute("session_user", user);
            model.addAttribute("user", user);
            return "user/protected";

        }

    }

    @GetMapping("/user/logout")
    public String destroySession(HttpServletRequest request) {

        request.getSession().invalidate();
        return "/user/login";

    }

}
