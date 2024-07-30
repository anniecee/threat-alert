package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepo;

    @MockBean
    private WebsiteRepository websiteRepo;

    @MockBean
    private CommentRepository commentRepo;

    private User user;
    private Website website;
    private MockHttpSession mockHttpSession;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUid(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");

        website = new Website();
        website.setWid(1);
        website.setLink("https://example.com");

        mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute("session_user", user);
        mockHttpSession.setAttribute("scanned_url", "https://example.com");
    }

    @Test
    public void testAddComment() throws Exception {
        // Tests the POST /addComment endpoint when adding a comment successfully
        // Mocks the repository behavior to simulate saving a comment
        // Verifies that the status is OK (200) and the response contains the comment ID

        String content = "This is a test comment";

        when(websiteRepo.findByLink("https://example.com")).thenReturn(List.of(website));
        when(commentRepo.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setCid(1);
            savedComment.setContent(content);
            return savedComment;
        });

        mockMvc.perform(post("/addComment")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"" + content + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

        verify(websiteRepo, times(1)).findByLink("https://example.com");
        verify(commentRepo, times(1)).save(any(Comment.class));
    }

    @Test
    public void testDeleteComment_Success() throws Exception {
        // Prepare a comment to delete
        Comment comment = new Comment(user, "Test comment", new Date(), website);
        comment.setCid(1);

        // Mock repository behavior
        when(commentRepo.findByCid(1)).thenReturn(List.of(comment));

        // Perform DELETE /deleteComment request
        mockMvc.perform(delete("/deleteComment")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isNoContent());

        // Verify repository interactions
        verify(commentRepo, times(1)).findByCid(1);
        verify(commentRepo, times(1)).delete(comment);
    }

    @Test
    public void testDeleteComment_Failure_UnauthorizedUser() throws Exception {
        // Prepare a comment to delete with a different user
        User otherUser = new User();
        otherUser.setUid(2);
        Comment comment = new Comment(otherUser, "Test comment", new Date(), website);
        comment.setCid(1);

        // Mock repository behavior
        when(commentRepo.findByCid(1)).thenReturn(List.of(comment));

        // Perform DELETE /deleteComment request
        mockMvc.perform(delete("/deleteComment")
                        .session(mockHttpSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("1"))
                .andExpect(status().isUnauthorized());

        // Verify repository interactions
        verify(commentRepo, times(1)).findByCid(1);
        verify(commentRepo, never()).delete(comment);
    }
}
