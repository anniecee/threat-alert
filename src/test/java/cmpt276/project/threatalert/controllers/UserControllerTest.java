package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.User;
import cmpt276.project.threatalert.models.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @Test
    public void testGetLoginUserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        mockMvc.perform(get("/user/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    public void testGetLoginAdminUserLoggedIn() throws Exception {
        User adminUser = new User("admin@example.com", "password", "admin");
        when(session.getAttribute("session_user")).thenReturn(adminUser);

        mockMvc.perform(get("/user/login").sessionAttr("session_user", adminUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/userview"));
    }

    @Test
    public void testGetLoginRegularUserLoggedIn() throws Exception {
        User regularUser = new User("user@example.com", "password");
        when(session.getAttribute("session_user")).thenReturn(regularUser);

        mockMvc.perform(get("/user/login").sessionAttr("session_user", regularUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scan.html"));
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        when(userRepository.findByEmailAndPassword("invalid@example.com", "password")).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/user/login")
                .param("email", "invalid@example.com")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    public void testLoginValidAdminCredentials() throws Exception {
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
        mockMvc.perform(get("/user/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/landing"));
    }

    @Test
    public void testGetSignup() throws Exception {
        mockMvc.perform(get("/user/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"));
    }

    @Test
    public void testSignupPasswordsDoNotMatch() throws Exception {
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
        when(userRepository.findByEmail("user@example.com")).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/user/signup")
                .param("email", "user@example.com")
                .param("password", "password")
                .param("passwordConfirm", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/scan.html"));
    }

    @Test
    public void testShowUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1@example.com", "password"));
        userList.add(new User("user2@example.com", "password"));
        when(userRepository.findAll()).thenReturn(userList);

        mockMvc.perform(get("/admin/userview"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userList"))
                .andExpect(view().name("admin/userview"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(post("/user/delete")
                .param("uid", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/userview"));
    }
}
