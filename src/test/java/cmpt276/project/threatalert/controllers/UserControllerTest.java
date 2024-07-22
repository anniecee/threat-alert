package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private WebsiteRepository websiteRepository;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        when(session.getAttribute("session_user")).thenReturn(null);
    }

    @Test
    public void testGetLoginUserNotLoggedIn() throws Exception {
        // Tests the GET /user/login endpoint when no user is logged in
        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    public void testGetLoginAdminUserLoggedIn() throws Exception {
        // Tests the GET /user/login endpoint when an admin user is logged in
        User adminUser = new User("admin@example.com", "password", "admin");
        when(session.getAttribute("session_user")).thenReturn(adminUser);

        mockMvc.perform(get("/user/login").sessionAttr("session_user", adminUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/userview"));
    }

    @Test
    public void testGetLoginRegularUserLoggedIn() throws Exception {
        // Tests the GET /user/login endpoint when a regular user is logged in
        User regularUser = new User("user@example.com", "password");
        when(session.getAttribute("session_user")).thenReturn(regularUser);

        mockMvc.perform(get("/user/login").sessionAttr("session_user", regularUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scan.html"));
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        // Tests the POST /user/login endpoint with invalid credentials
        when(userRepository.findByEmailAndPassword("invalid@example.com", "password")).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/user/login")
                .param("email", "invalid@example.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testLoginValidAdminCredentials() throws Exception {
        // Tests the POST /user/login endpoint with valid admin credentials
        User adminUser = new User("admin@example.com", "password", "admin");
        List<User> userList = new ArrayList<>();
        userList.add(adminUser);
        when(userRepository.findByEmailAndPassword("admin@example.com", "password")).thenReturn(userList);

        mockMvc.perform(post("/user/login")
                .param("email", "admin@example.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/userview"));
    }

    @Test
    public void testLoginValidRegularUserCredentials() throws Exception {
        // Tests the POST /user/login endpoint with valid regular user credentials
        User regularUser = new User("user@example.com", "password");
        List<User> userList = new ArrayList<>();
        userList.add(regularUser);
        when(userRepository.findByEmailAndPassword("user@example.com", "password")).thenReturn(userList);

        mockMvc.perform(post("/user/login")
                .param("email", "user@example.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scan.html"));
    }

    @Test
    public void testLogout() throws Exception {
        // Tests the GET /user/logout endpoint
        mockMvc.perform(get("/user/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/landing"));
    }

    @Test
    public void testGetSignup() throws Exception {
        // Tests the GET /user/signup endpoint
        mockMvc.perform(get("/user/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }

    @Test
    public void testSignupPasswordsDoNotMatch() throws Exception {
        // Tests the POST /user/signup endpoint when passwords do not match
        mockMvc.perform(post("/user/signup")
                .param("email", "user@example.com")
                .param("password", "password")
                .param("passwordConfirm", "differentPassword"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("user/signup"));
    }

    @Test
    public void testSignupEmailAlreadyExists() throws Exception {
        // Tests the POST /user/signup endpoint when the email already exists
        User existingUser = new User("user@example.com", "password");
        List<User> userList = new ArrayList<>();
        userList.add(existingUser);
        when(userRepository.findByEmail("user@example.com")).thenReturn(userList);

        mockMvc.perform(post("/user/signup")
                .param("email", "user@example.com")
                .param("password", "password")
                .param("passwordConfirm", "password"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("user/signup"));
    }

    @Test
    public void testSignupSuccess() throws Exception {
        // Tests the POST /user/signup endpoint with a successful signup
        when(userRepository.findByEmail("user@example.com")).thenReturn(new ArrayList<>());
    
        mockMvc.perform(post("/user/signup")
                .param("email", "user@example.com")
                .param("password", "password!@#")
                .param("passwordConfirm", "password!@#"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }
    

    @Test
    public void testShowUsersAdmin() throws Exception {
        // Tests the GET /admin/userview endpoint for an admin user
        User adminUser = new User("admin@example.com", "password", "admin");
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1@example.com", "password"));
        userList.add(new User("user2@example.com", "password"));
        when(session.getAttribute("session_user")).thenReturn(adminUser);
        when(userRepository.findAll()).thenReturn(userList);

        mockMvc.perform(get("/admin/userview").sessionAttr("session_user", adminUser))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userList"))
                .andExpect(view().name("admin/userview"));
    }

    @Test
    public void testShowUsersNotAdmin() throws Exception {
        // Tests the GET /admin/userview endpoint for a non-admin user
        User regularUser = new User("user@example.com", "password");
        when(session.getAttribute("session_user")).thenReturn(regularUser);

        mockMvc.perform(get("/admin/userview").sessionAttr("session_user", regularUser))
                .andExpect(status().isOk())
                .andExpect(view().name("user/invalid"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Tests the POST /user/delete endpoint
        mockMvc.perform(post("/user/delete")
                .param("uid", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/userview"));
    }

    @Test
    public void testAddHistory() throws Exception {
        // Tests the POST /user/addhistory endpoint
        User user = new User("user@example.com", "password");
        user.setUid(1);
        Website website = new Website();
        website.setWid(1);
        website.setLink("http://example.com");
        website.setThreatlevel("Clean");
        website.setDate(new Date());

        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findByUid(1)).thenReturn(List.of(user));
        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(post("/user/addhistory")
                .sessionAttr("session_user", user)
                .contentType("application/json")
                .content("{\"link\":\"http://example.com\", \"threatlevel\":\"Clean\"}"))
                .andExpect(status().isOk());
    }

    
    

    @Test
    public void testViewHistory() throws Exception {
        // Tests the GET /user/history endpoint
        User user = new User("user@example.com", "password");
        user.setUid(1);
        List<Website> history = new ArrayList<>();
        history.add(new Website("http://example.com", "Clean"));
        user.setHistory(history);

        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findByUid(1)).thenReturn(List.of(user));

        mockMvc.perform(get("/user/history").sessionAttr("session_user", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("history"))
                .andExpect(view().name("user/history"));
    }

    @Test
    public void testDeleteWebsite() throws Exception {
        // Tests the POST /user/deletewebsite endpoint
        Website website = new Website();
        website.setWid(1);
        website.setLink("http://example.com");
        website.setThreatlevel("Clean");
        User user = new User("user@example.com", "password");
        user.setUid(1);
        website.setUser(user);

        when(websiteRepository.findByWid(1)).thenReturn(website);
        when(userRepository.save(user)).thenReturn(user);

        mockMvc.perform(post("/user/deletewebsite")
                .param("wid", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/history"));
    }
}