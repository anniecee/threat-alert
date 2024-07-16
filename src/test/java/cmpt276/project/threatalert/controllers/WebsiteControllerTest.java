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
        mockMvc.perform(get("/landing"))
                .andExpect(status().isOk())
                .andExpect(view().name("website/landing"));
    }

    @Test
    public void testProcess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("landing"));
    }

    @Test
    public void testBack() throws Exception {
        mockMvc.perform(get("/back"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/landing"));
    }
}