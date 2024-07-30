package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.Comment;
import cmpt276.project.threatalert.models.CommentRepository;
import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.util.Date;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;



@RestController
public class CommentController {

    @Autowired
    private UserRepository userRepo;

    @Autowired 
    private WebsiteRepository websiteRepo;

    @Autowired
    private CommentRepository commentRepo;

    @PostMapping("/addComment")
@ResponseBody
public ResponseEntity<Integer> addComment(@RequestBody Map<String, String> formData, HttpSession session, HttpServletResponse response) {
    Date newDate = new Date();

    // Get current user
    User user = (User) session.getAttribute("session_user");
    System.out.println("Session user received: " + user);

    // Get current website
    String link = (String) session.getAttribute("scanned_url");
    System.out.println("Session URL received for comments: " + link);

    // Check if link is already in repository
    List<Website> websiteList = websiteRepo.findByLink(link);
    Website website = websiteList.get(0);

    // Get comment content
    String content = formData.get("content");
    if (content.length() == 0) {
        System.out.println("No content in comment");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
    } else {
        Comment comment = new Comment(user, content, newDate, website);
        website.addComment(comment);
        commentRepo.save(comment);
        websiteRepo.save(website); // Save the updated website

        return ResponseEntity.ok(comment.getCid());
    }
}
    
    @DeleteMapping("/deleteComment")
    @ResponseBody
    public String deleteComment(@RequestBody int cid, HttpSession session, HttpServletResponse response) {

            // Return message
            String message;
            // Get current user
            User user = (User) session.getAttribute("session_user");
            int Uid = user.getUid();
            System.out.println("user id " + Uid);
            System.out.println("comment id: " +cid);
            

            // Get comment ID and check if it's from user, 
            List<Comment>commentList = commentRepo.findByCid(cid);
            Comment commenttoDelete = commentList.get(0);

            // If yes, delete
            if (commenttoDelete.getUser().getUid() == Uid){

                System.out.println("comment matched with user");
                commentRepo.delete(commenttoDelete);
                response.setStatus(204);

                message = "Removed from database";

                return message;
            }       

            response.setStatus(401);
            message = "Unauthorized user";

            return message;
    }
}

