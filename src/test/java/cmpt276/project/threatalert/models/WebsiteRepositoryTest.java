package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WebsiteRepositoryTest {

    @Mock
    private WebsiteRepository websiteRepository;

    @InjectMocks
    private WebsiteRepositoryTest websiteRepositoryTest;

    @BeforeEach
    public void setUp() {
        // Initializes mock objects before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByLink() {
        // Tests the findByLink method of the WebsiteRepository
        String link = "http://example.com";
        Website website = new Website(link, "Clean");
        List<Website> expectedWebsites = new ArrayList<>();
        expectedWebsites.add(website);

        // Sets up the mock behavior
        when(websiteRepository.findByLink(link)).thenReturn(expectedWebsites);

        // Calls the method to test
        List<Website> foundWebsites = websiteRepository.findByLink(link);

        // Asserts the expected behavior
        assertEquals(1, foundWebsites.size());
        assertEquals(link, foundWebsites.get(0).getLink());
        assertEquals("Clean", foundWebsites.get(0).getThreatlevel());
    }

    @Test
    public void testFindByWid() {
        // Tests the findByWid method of the WebsiteRepository
        int wid = 1;
        Website website = new Website("http://example.com", "Clean");
        website.setWid(wid);
        List<Website> expectedWebsites = new ArrayList<>();
        expectedWebsites.add(website);

        when(websiteRepository.findByWid(wid)).thenReturn(expectedWebsites);

        List<Website> foundWebsites = websiteRepository.findByWid(wid);

        assertEquals(1, foundWebsites.size());
        assertEquals(wid, foundWebsites.get(0).getWid());
    }

    @Test
    public void testFindByLinkNoResult() {
        // Tests the findByLink method of the WebsiteRepository when no result is found
        String link = "http://notfound.com";
        List<Website> expectedWebsites = new ArrayList<>();

        when(websiteRepository.findByLink(link)).thenReturn(expectedWebsites);

        List<Website> foundWebsites = websiteRepository.findByLink(link);

        assertTrue(foundWebsites.isEmpty());
    }
}
