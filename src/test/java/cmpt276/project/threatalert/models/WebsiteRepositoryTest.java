package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebsiteRepositoryTest {

    @Mock
    private WebsiteRepository websiteRepository;

    private Website website;

    @BeforeEach
    public void setUp() {
        website = new Website();
        website.setWid(1);
        website.setLink("https://example.com");
    }

    // Test finding a Website by WID
    @Test
    public void testFindByWid() {
        when(websiteRepository.findByWid(1)).thenReturn(List.of(website));

        List<Website> foundWebsites = websiteRepository.findByWid(1);

        assertFalse(foundWebsites.isEmpty());
        assertEquals(1, foundWebsites.size());
        assertEquals(website.getWid(), foundWebsites.get(0).getWid());
        assertEquals(website.getLink(), foundWebsites.get(0).getLink());

        verify(websiteRepository, times(1)).findByWid(1);
    }

    // Test finding a Website by link
    @Test
    public void testFindByLink() {
        when(websiteRepository.findByLink("https://example.com")).thenReturn(List.of(website));

        List<Website> foundWebsites = websiteRepository.findByLink("https://example.com");

        assertFalse(foundWebsites.isEmpty());
        assertEquals(1, foundWebsites.size());
        assertEquals(website.getWid(), foundWebsites.get(0).getWid());
        assertEquals(website.getLink(), foundWebsites.get(0).getLink());

        verify(websiteRepository, times(1)).findByLink("https://example.com");
    }

    // Test finding a Website by a non-existent WID
    @Test
    public void testFindByNonExistentWid() {
        when(websiteRepository.findByWid(999)).thenReturn(List.of());

        List<Website> foundWebsites = websiteRepository.findByWid(999);

        assertTrue(foundWebsites.isEmpty());

        verify(websiteRepository, times(1)).findByWid(999);
    }

    // Test finding a Website by a non-existent link
    @Test
    public void testFindByNonExistentLink() {
        when(websiteRepository.findByLink("https://nonexistent.com")).thenReturn(List.of());

        List<Website> foundWebsites = websiteRepository.findByLink("https://nonexistent.com");

        assertTrue(foundWebsites.isEmpty());

        verify(websiteRepository, times(1)).findByLink("https://nonexistent.com");
    }
}