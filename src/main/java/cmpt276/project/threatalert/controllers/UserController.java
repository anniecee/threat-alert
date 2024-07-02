package cmpt276.project.threatalert.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


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

            //show admin page if user is an admin
            if (user.getType().equals("admin")) {
                return "redirect:/admin/userview";
            } 
            //show scan page for regular user
            else {
                return "redirect:/scan.html";
            }

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

            //show admin page if user is an admin
            if (user.getType().equals("admin")) {
                return "redirect:/admin/userview";
            } 
            //show scan page for regular user
            else {
                return "redirect:/scan.html";
            }

        }

    }

    @GetMapping("/user/logout")
    public String destroySession(HttpServletRequest request) {

        request.getSession().invalidate();
        return "redirect:/landing";

    }

    @GetMapping("/user/signup")
    public String getSignup() {

        return "user/signup";

    }

    @PostMapping("/user/signup")
    public String signup(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session, HttpServletResponse response) {

        String email = formData.get("email");
        String password = formData.get("password");
        String confirmedPassword = formData.get("passwordConfirm");

        if (!password.equals(confirmedPassword)) {

            model.addAttribute("error", "Passwords do not match");
            return "user/signup";

        }

        //check if email is already in repository
        List<User> userList = userRepo.findByEmail(email);

        //if not in there
        if (userList.isEmpty()) { //add to database and redirect to home page
            
            User user = new User(email, confirmedPassword);
            userRepo.save(user);
            response.setStatus(201);
            return "redirect:/scan.html";

        //if in there, redirect back to login saying already have an account
        } else {

            model.addAttribute("error", "Account linked with email already exists");
            return "user/signup";

        }

    }

    @GetMapping("/admin/userview")
    public String showUsers(Model model) {

        List<User> userList = userRepo.findAll();

        model.addAttribute("userList", userList);

        return "admin/userview";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("uid") int uid, HttpServletResponse response) {

        userRepo.deleteById(uid);
        response.setStatus(202);

        return "redirect:/admin/userview";
    }
    
    

}
