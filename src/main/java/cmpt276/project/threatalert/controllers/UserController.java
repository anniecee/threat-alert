package cmpt276.project.threatalert.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cmpt276.project.threatalert.models.Scan;
import cmpt276.project.threatalert.models.ScanRepository;
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

    @Autowired
    private ScanRepository scanRepo;

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
                // return "redirect:/scan.html";
                return "scan/urlscan";
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
                // return "redirect:/scan.html";
                return "redirect:/home";
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
            response.setStatus(HttpServletResponse.SC_CREATED);

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

    // public void addHistory(@RequestBody Website website, HttpSession session, HttpServletResponse response) {
        
    //     System.out.println("adding history");

    //     User user = (User) session.getAttribute("session_user");
    //     if (user == null) {
    //         response.setStatus(400);
    //         return;
    //     }

    //     user = userRepo.findByUid(user.getUid()).get(0);

    //     website.setUser(user);
    //     websiteRepo.save(website);

    //     user.addHistory(website);
    //     userRepo.save(user);

    //     response.setStatus(200);
 
    // }

    /*  */

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam("uid") int uid, HttpServletResponse response) {

        List<User> users = userRepo.findByUid(uid);

        if (users.isEmpty()) {
            return "redirect:/admin/userview";
        }

        User user = users.get(0);

        List<Scan> scans = user.getScans();
        for (Scan scan : scans) {
            scanRepo.delete(scan);
        }

        userRepo.delete(user);
        response.setStatus(HttpServletResponse.SC_GONE);

        return "redirect:/admin/userview";
    }
    
    @GetMapping("/user/history")
    public String viewHistory(Model model, HttpSession session) {

        System.out.println("viewing history");

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }
        
        user = userRepo.findByUid(user.getUid()).get(0);
        List<Scan> scans = user.getScans();
        List<Scan> history = new ArrayList<>();

        if (scans.size() > 0) {
            for (Scan scan : scans) {
                if (!scan.isToDelete()) {
                    history.add(scan);
                }
            }
            sortByScanDate(history);
        }

        model.addAttribute("history", history);

        return "user/history";
    }

    //for viewing scans
    private void sortByScanDate(List<Scan> scans) {
        Collections.sort(scans, new Comparator<Scan>() {
            @Override
            public int compare(Scan s1, Scan s2) {
                return s2.getScanDate().compareTo(s1.getScanDate()); //desc order
            }
        });
    }
    
    // Receives Delete Mapping request from website.js, and returns response back to it
    @DeleteMapping("/user/deletefromhistory")
    @ResponseBody
    public String deleteWebsite(@RequestBody String sid, HttpServletResponse response) {

        System.out.println("received wid: " + sid);
        int sidInt = Integer.parseInt(sid);
        List<Scan> scans = scanRepo.findBySid(sidInt);
        String message;

        if (!scans.isEmpty()) {
            Scan scan = scans.get(0);

            User user = scan.getUser();
            System.out.println("removing: " + scan.getWebsite().getLink() + " " + scan.getScanDate());
            if (scan.isBookmark()) {
                scan.setToDelete(true);
                scanRepo.save(scan);

                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                message = "Removed from history, but still bookmarked";
            }
            else {
                user.removeScan(scan);
                userRepo.save(user);
                scanRepo.delete(scan);
    
                response.setStatus(HttpServletResponse.SC_GONE);
                message = "Received " + sid + " and deleted";
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message = "Received " + sid + " but unable to delete";
        }

        return message;
    }

    @GetMapping("/user/bookmarks")
    public String viewBookmarks(Model model, HttpSession session) {

        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/user/login";
        }

        user = userRepo.findByUid(user.getUid()).get(0);
        List<Scan> scans = user.getScans();
        List<Scan> bookmarks = new ArrayList<>();

        if (scans.size() > 0) {
            for (Scan scan : scans) {
                if (scan.isBookmark()) {
                    bookmarks.add(scan);
                }
            }
            sortByBookmarkDate(bookmarks);
        }

        model.addAttribute("bookmarks", bookmarks);

        return "user/bookmarks";
    }

    // for viewing bookmarks
    private void sortByBookmarkDate(List<Scan> scans) {
        Collections.sort(scans, new Comparator<Scan>() {
            @Override
            public int compare(Scan s1, Scan s2) {
                return s2.getBookmarkDate().compareTo(s1.getBookmarkDate()); //desc order
            }
        });
    }

    // Put mapping from website.js, sets scan.bookmark to true
    @PutMapping("/user/addbookmark")
    @ResponseBody
    public String addBookmark(@RequestBody String sid, HttpServletResponse response) {

        System.out.println("received wid: " + sid);
        int sidInt = Integer.parseInt(sid);
        List<Scan> scans = scanRepo.findBySid(sidInt);
        String message;

        if (!scans.isEmpty()) {
            Scan scan = scans.get(0);

            System.out.println("bookmarking " + sid);
            if (scan.isBookmark()) {
                response.setStatus(HttpServletResponse.SC_ACCEPTED);
                message = "Item has already been bookmarked";
            }
            else {
                scan.setBookmark(true);
                scan.setBookmarkDate(new Date());
                scanRepo.save(scan);
                response.setStatus(HttpServletResponse.SC_OK);
                message = "Item successfully bookmarked";
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message = "Item not found, bookmark failed";
        }

        return message;
    }

    @DeleteMapping("/user/removebookmark")
    @ResponseBody
    public String removeBookmark(@RequestBody String sid, HttpServletResponse response) {

        int sidInt = Integer.parseInt(sid);
        List<Scan> scans = scanRepo.findBySid(sidInt);
        String message;

        if (!scans.isEmpty()) {
            Scan scan = scans.get(0);

            if (scan.isToDelete()) {
                User user = scan.getUser();
                user.removeScan(scan);
                userRepo.save(user);
                scanRepo.delete(scan);
                response.setStatus(HttpServletResponse.SC_GONE);

                message = "Removed from database";
            }
            else {
                scan.setBookmark(false);
                scan.setBookmarkDate(null);
                scanRepo.save(scan);
                response.setStatus(HttpServletResponse.SC_ACCEPTED);

                message = "Removed from bookmarks";
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message = "Item not found";
        }

        return message;
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
