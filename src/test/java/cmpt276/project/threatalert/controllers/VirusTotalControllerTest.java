package cmpt276.project.threatalert.controllers;

import cmpt276.project.threatalert.models.*;
import cmpt276.project.threatalert.services.OpenAIService;
import cmpt276.project.threatalert.services.VirusTotalService;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VirusTotalController.class)
public class VirusTotalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VirusTotalService virusTotalService;

    @MockBean
    private OpenAIService openAIService;

    @MockBean
    private WebsiteRepository websiteRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ScanRepository scanRepository;

    @Mock
    private HttpSession session;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("user", "user@example.com", "password");
        user.setUid(1);
    }

    @Test
    public void testScanUrlUserNotLoggedIn() throws Exception {
        // Test to verify the behavior when a user is not logged in; expects a rediretion to the login page
        mockMvc.perform(post("/scan")
                .param("url", "http://example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login"));
    }

    @Test
    public void testScanUrlInvalidUrl() throws Exception {
        // Test to verify the behavior when an invalid URL is provided; expects an error message to be added to the model and the view to be "scan/urlscan"
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findById(user.getUid())).thenReturn(Optional.of(user));
        when(virusTotalService.scanUrl("http://invalid-url")).thenReturn("{\"error\":\"Invalid URL\"}");

        mockMvc.perform(post("/scan")
                .sessionAttr("session_user", user)
                .param("url", "http://invalid-url"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Error reading URL. Please make sure to enter a valid URL."))
                .andExpect(view().name("scan/urlscan"));
    }

    @Test
    public void testScanUrlValidUrl() throws Exception {
        // Test to verify the behavior when a valid URL is provided; expects the scan result to be added to the model and the view to be "scan/urlscan". 
        // Additionally, it verifies that the scan, website, and user repositories save the expected objects.
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findById(user.getUid())).thenReturn(Optional.of(user));
        when(virusTotalService.scanUrl("http://example.com")).thenReturn(getValidScanResultJson());

        mockMvc.perform(post("/scan")
                .sessionAttr("session_user", user)
                .param("url", "http://example.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("website"))
                .andExpect(model().attributeExists("result"))
                .andExpect(view().name("scan/urlscan"));

        verify(scanRepository, times(1)).save(any(Scan.class));
        verify(websiteRepository, times(1)).save(any(Website.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testScanUrlIOException() throws Exception {
        // Test to verify the behavior when an IOException occurs during URL scanning; expects an error message to be added to the model and the view to be "scan/urlscan".
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepository.findById(user.getUid())).thenReturn(Optional.of(user));
        when(virusTotalService.scanUrl("http://example.com")).thenThrow(new IOException("Service not available"));

        mockMvc.perform(post("/scan")
                .sessionAttr("session_user", user)
                .param("url", "http://example.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"))
                .andExpect(model().attribute("result", "Error: Service not available"))
                .andExpect(view().name("scan/urlscan"));
    }

    // Helper method to generate a valid JSON string representing a scan result
    private String getValidScanResultJson() {
        JsonObject jsonObject = new JsonObject();
        JsonObject data = new JsonObject();
        JsonObject attributes = new JsonObject();
        JsonObject stats = new JsonObject();
        JsonObject results = new JsonObject();

        stats.addProperty("malicious", 1);
        stats.addProperty("suspicious", 0);
        stats.addProperty("undetected", 69);
        stats.addProperty("harmless", 2);
        stats.addProperty("timeout", 0);

        JsonObject vendorResult = new JsonObject();
        vendorResult.addProperty("engine_name", "Vendor1");
        vendorResult.addProperty("category", "harmless");
        vendorResult.addProperty("result", "clean");

        results.add("Vendor1", vendorResult);

        attributes.add("stats", stats);
        attributes.add("results", results);
        data.add("attributes", attributes);
        jsonObject.add("data", data);

        return jsonObject.toString();
    }
}
