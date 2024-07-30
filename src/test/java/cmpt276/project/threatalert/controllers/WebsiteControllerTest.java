package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebsiteController.class)
public class WebsiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Model model;

    private User user;
    private MockHttpSession httpSession;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUid(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");

        httpSession = new MockHttpSession();
        httpSession.setAttribute("session_user", user);
    }

    @Test
    public void testLanding() throws Exception {
        // Tests the GET /landing endpoint
        // Verifies that the status is OK (200) and the view name is "website/landing"
        mockMvc.perform(get("/landing"))
                .andExpect(status().isOk())
                .andExpect(view().name("website/landing"));
    }

    @Test
    public void testProcess() throws Exception {
        // Tests the GET / endpoint
        // Verifies that the status is a redirection (3xx) and the redirected URL is
        // "landing"
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("landing"));
    }

    @Test
    public void testBack() throws Exception {
        // Tests the GET /back endpoint
        // Verifies that the status is a redirection (3xx) and the redirected URL is
        // "/landing"
        mockMvc.perform(get("/back"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/landing"));
    }

    @Test
    public void testHome_UserInSession() throws Exception {
        // Tests the GET /home endpoint when the user is in session
        // Verifies that the status is OK (200), the view name is "scan/urlscan",
        // and the "user" attribute is added to the model
        mockMvc.perform(get("/home").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("scan/urlscan"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    public void testHome_UserNotInSession() throws Exception {
        // Tests the GET /home endpoint when the user is not in session
        // Verifies that the status is a redirection (3xx) and the view name is
        // "redirect:/user/login"
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/login"));
    }

    @Test
    public void testFscan_UserInSession() throws Exception {
        // Tests the GET /filescan endpoint when the user is in session
        // Verifies that the status is OK (200), the view name is "scan/filescan",
        // and the "user" attribute is added to the model
        mockMvc.perform(get("/filescan").session(httpSession))
                .andExpect(status().isOk())
                .andExpect(view().name("scan/filescan"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    public void testFscan_UserNotInSession() throws Exception {
        // Tests the GET /filescan endpoint when the user is not in session
        // Verifies that the status is a redirection (3xx) and the view name is
        // "redirect:/user/login"
        mockMvc.perform(get("/filescan"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/login"));
    }
}
