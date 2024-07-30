package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.Scan;
import cmpt276.project.threatalert.models.ScanRepository;
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

    @MockBean
    private ScanRepository scanRepository;

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
        // Test the GET /user/login endpoint when a regular user is logged in
        User regularUser = new User("user", "user@example.com", "password");
        when(session.getAttribute("session_user")).thenReturn(regularUser);

        mockMvc.perform(get("/user/login").sessionAttr("session_user", regularUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
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
        User regularUser = new User("user", "user@example.com", "password");
        List<User> userList = new ArrayList<>();
        userList.add(regularUser);
        when(userRepository.findByEmailAndPassword("user@example.com", "password")).thenReturn(userList);

        mockMvc.perform(post("/user/login")
                .param("email", "user@example.com")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
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
        User existingUser = new User("user", "user@example.com", "password");
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
        userList.add(new User("user", "user1@example.com", "password"));
        userList.add(new User("user", "user2@example.com", "password"));
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
        User regularUser = new User("user", "user@example.com", "password");
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

    // @Test
    // public void testAddHistory() throws Exception {
    // // Tests the POST /user/addhistory endpoint
    // Date date = new Date();
    // User user = new User("user", date, "user@example.com", "password");
    // user.setUid(1);
    // Website website = new Website();
    // website.setWid(1);
    // website.setLink("http://example.com");
    // website.setThreatlevel("Clean");
    // website.setDate(new Date());

    // when(session.getAttribute("session_user")).thenReturn(user);
    // when(userRepository.findByUid(1)).thenReturn(List.of(user));
    // when(userRepository.save(user)).thenReturn(user);

    // mockMvc.perform(post("/user/addhistory")
    // .sessionAttr("session_user", user)
    // .contentType("application/json")
    // .content("{\"link\":\"http://example.com\", \"threatlevel\":\"Clean\"}"))
    // .andExpect(status().isOk());
    // }

    @Test
    public void testViewHistory() throws Exception {
        // Test to verify the behavior of viewing user history.
        // Sets up a user with a scan history and verifies that the history is correctly added to the model and the view is "user/history".
        User user = new User("user", "user@example.com", "password");
        user.setUid(1);

        Website website = new Website("http://example.com", "Clean");
        Scan scan = new Scan(website);
        List<Scan> scans = new ArrayList<>();
        scans.add(scan);
        user.setScans(scans);

        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findByUid(1)).thenReturn(List.of(user));

        mockMvc.perform(get("/user/history").sessionAttr("session_user", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("history"))
                .andExpect(view().name("user/history"));
    }

    @Test
    public void testDeleteWebsite() throws Exception {
        // Test to verify the behavior of deleting a scan from user history.
        // Sets up a scan with a given sid and verifies that the scan is correctly deleted from the repository and the status is 410 Gone.
        int sid = 1;
        User user = new User("user", "user@example.com", "password");
        user.setUid(1);

        Website website = new Website("http://example.com", "Clean");
        Scan scan = new Scan(website);
        scan.setSid(sid);
        scan.setUser(user);
        List<Scan> scans = new ArrayList<>();
        scans.add(scan);

        when(scanRepository.findBySid(sid)).thenReturn(scans);

        mockMvc.perform(delete("/user/deletefromhistory")
                .contentType("application/json")
                .content(String.valueOf(sid)))
                .andExpect(status().isGone());

        verify(scanRepository, times(1)).delete(scan);
    }

    @Test
    public void testViewBookmarks() throws Exception {
        // Test to verify the behavior of viewing user bookmarks.
        // Sets up a user with bookmarked scans and verifies that the bookmarks are correctly added to the model and the view is "user/bookmarks".
        User user = new User("user", "user@example.com", "password");
        user.setUid(1);

        Website website = new Website("http://example.com", "Clean");
        Scan scan = new Scan(website);
        List<Scan> scans = new ArrayList<>();
        scan.setBookmark(true);
        scans.add(scan);
        user.setScans(scans);

        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findByUid(1)).thenReturn(List.of(user));

        mockMvc.perform(get("/user/bookmarks").sessionAttr("session_user", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookmarks"))
                .andExpect(view().name("user/bookmarks"));
    }

    @Test
    public void testAddBookmark() throws Exception {
        // Test to verify the behavior of adding a bookmark to a scan.
        // Sets up a scan with a given sid and verifies that the scan is correctly saved in the repository with a bookmark status.
        int sid = 1;
        Scan scan = new Scan();
        scan.setSid(sid);
        List<Scan> scans = new ArrayList<>();
        scans.add(scan);

        when(scanRepository.findBySid(sid)).thenReturn(scans);

        mockMvc.perform(put("/user/addbookmark")
                .contentType("application/json")
                .content(String.valueOf(sid)))
                .andExpect(status().isOk());

        verify(scanRepository, times(1)).save(scan);
    }

    @Test
    public void testRemoveBookmark() throws Exception {
        // Test to verify the behavior of removing a bookmark from a scan.
        // Sets up a scan with a given sid and verifies that the scan is correctly saved in the repository without a bookmark status.
        int sid = 1;
        Scan scan = new Scan();
        scan.setSid(sid);
        List<Scan> scans = new ArrayList<>();
        scans.add(scan);

        when(scanRepository.findBySid(sid)).thenReturn(scans);

        mockMvc.perform(delete("/user/removebookmark")
                .contentType("application/json")
                .content(String.valueOf(sid)))
                .andExpect(status().isAccepted());

        verify(scanRepository, times(1)).save(scan);
    }

    @Test
    public void testViewProfile() throws Exception {
        // Test to verify the behavior of viewing user profile.
        // Sets up a user and verifies that the user profile is correctly added to the model and the view is "user/profile".
        User user = new User("user", "user@example.com", "password");
        user.setUid(1);

        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findByUid(1)).thenReturn(List.of(user));

        mockMvc.perform(get("/user/profile").sessionAttr("session_user", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/profile"));
    }
}