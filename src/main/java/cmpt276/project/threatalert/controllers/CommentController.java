// package cmpt276.project.threatalert.controllers;

// import org.springframework.web.bind.annotation.RestController;

// import cmpt276.project.threatalert.models.Comment;
// import cmpt276.project.threatalert.models.CommentRepository;
// import cmpt276.project.threatalert.models.User;
// import cmpt276.project.threatalert.models.UserRepository;
// import cmpt276.project.threatalert.models.Website;
// import cmpt276.project.threatalert.models.WebsiteRepository;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;

// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import java.util.Map;

// import org.springframework.web.servlet.mvc.support.RedirectAttributes;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.GetMapping;



// @Controller
// public class CommentController {

//     @Autowired
//     private UserRepository userRepo;

//     @Autowired 
//     private WebsiteRepository websiteRepo;

//     @Autowired
//     private CommentRepository commentRepo;

//     @PostMapping("/addComment")
//     public ResponseEntity<?> addComment(@RequestBody Map<String, String> formData, Model model, HttpSession session, HttpServletResponse response) {

//         System.out.println("formData: " + formData);
//         Date newDate = new Date();
//         // Get current user
//         User user = (User) session.getAttribute("session_user");

//         // Get current website
//         // String link = formData.get("url"); 
//         String link = (String) session.getAttribute("scanned_url");

//         //check if link is already in repository
//         List<Website> websiteList = websiteRepo.findByLink(link);

//         // if no, create the website object with an empty list of comments
//         // Website website;
//         // if (websiteList.isEmpty()) {
//         //     List<Comment> newcomments = new ArrayList<>();
//         //     website = new Website(newDate, link, newcomments);
//         //     websiteRepo.save(website);
//         // }
//         // else{
//         //     website = websiteList.get(0);
//         // }

//         Website website = websiteList.get(0);

//         // Get comment content
//         String content = formData.get("commentInput");
//         if (content.length() == 0){
//             model.addAttribute("error", "No Comment Input");
//             response.setStatus(400);
//         }

//         Comment comment = new Comment(user, content, newDate, website);

//         website.addComment(comment);
//         commentRepo.save(comment);
//         websiteRepo.save(website); // Save the updated website

//         return ResponseEntity.ok(comment);
//     }
    


// }