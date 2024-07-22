package cmpt276.project.threatalert.controllers;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired 
    private WebsiteRepository websiteRepo;

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
            
            model.addAttribute("error", "Invalid Email or Password.");
            return "user/login";

        } else {

            User user = userList.get(0);

            session.invalidate();

            session = request.getSession(true);
            session.setAttribute("session_user", user);

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
    public String signup(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        String email = formData.get("email");
        String password = formData.get("password");
        String confirmedPassword = formData.get("passwordConfirm");

        if (!password.equals(confirmedPassword)) {

            model.addAttribute("error", "Passwords Do Not Match.");
            return "user/signup";

        }

        //password restrictions: minimum length of 8 & at least one special character
        if (password.length() < 8 || !password.matches(".*[!@#$%^&*()-].*")) {
            model.addAttribute("error", "Password Must Be At Least 8 Characters & Contain At Least One Special Character.");
            return "user/signup";
        }

        //check if email is already in repository
        List<User> userList = userRepo.findByEmail(email);

        //if not in there
        if (userList.isEmpty()) { //add to database and redirect to home page
            
            User user = new User(email, confirmedPassword);
            userRepo.save(user);
            request.getSession().setAttribute("session_user", user);
            response.setStatus(201);

            redirectAttributes.addFlashAttribute("signupSuccess", true);
            return "redirect:/user/login";

        //if in there, redirect back to login saying already have an account
        } else {

            model.addAttribute("error", "Account Linked With Email Already Exists.");
            return "user/signup";

        }

    }

    @GetMapping("/admin/userview")
    public String showUsers(Model model, HttpSession session) {

        User user = (User)session.getAttribute("session_user");
        //if not logged in, take to login page
        if (user == null) {
            return "/user/login";
        }
        //if user is admin, show user view page
        else if (user.getType().equalsIgnoreCase("admin")) {
        
            List<User> userList = userRepo.findAll();
            model.addAttribute("userList", userList);
            return "admin/userview";

        }
        // if not admin, go to home
        else {
            return "user/invalid";
        }
        
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("uid") int uid, HttpServletResponse response) {

        userRepo.deleteById(uid);
        response.setStatus(202);

        return "redirect:/admin/userview";
    }
    
    @PostMapping("/user/addhistory")
    public void addHistory(@RequestBody Website website, HttpSession session, HttpServletResponse response) {
        
        System.out.println("adding history");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            response.setStatus(400);
            return;
        }

        user = userRepo.findByUid(user.getUid()).get(0);

        website.setDate(new Date());
        website.setUser(user);
        websiteRepo.save(website);

        user.addHistory(website);
        userRepo.save(user);

        response.setStatus(200);

    }
    
    @GetMapping("/user/history")
    public String viewHistory(Model model, HttpSession session) {

        System.out.println("viewing history");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }
        
        user = userRepo.findByUid(user.getUid()).get(0);

        List<Website> history = user.getHistory();
        model.addAttribute("history", history);

        return "user/history";
    }
    
    @PostMapping("/user/deletewebsite")
    public String deleteWebsite(@RequestParam("wid") int wid, HttpServletResponse response) {
        
        Website website = websiteRepo.findByWid(wid);

        User user = website.getUser();
        System.out.println("removing: " + website.getLink() + " " + website.getDate());
        user.getHistory().remove(website);
        userRepo.save(user);
        
        websiteRepo.delete(website);
        response.setStatus(202);
        
        return "redirect:/user/history";
    }

    @GetMapping("/user/profile")
    public String viewProfile(Model model, HttpSession session) {

        System.out.println("viewing profile");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }
        
        user = userRepo.findByUid(user.getUid()).get(0);

        model.addAttribute("user", user);

        return "user/profile";
    }
}
