package cmpt276.project.threatalert.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebsiteController.class)
public class WebsiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
        // Verifies that the status is a redirection (3xx) and the redirected URL is "landing"
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("landing"));
    }

    @Test
    public void testBack() throws Exception {
        // Tests the GET /back endpoint
        // Verifies that the status is a redirection (3xx) and the redirected URL is "/landing"
        mockMvc.perform(get("/back"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/landing"));
    }

    @Test
    public void testHome() throws Exception {
        // Perform GET request to /hom
        // Verifies that status is OK (200) and the view name to be "scan/urlscan"
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("scan/urlscan"));
    }

    @Test
    public void testScan() throws Exception {
        // Perform GET request to /scan
        // Verifies that status is OK (200) and the view name to be "scan/urlscan"
        mockMvc.perform(get("/scan"))
                .andExpect(status().isOk())
                .andExpect(view().name("scan/urlscan"));
    }
}
