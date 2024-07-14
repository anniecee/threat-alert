package cmpt276.project.threatalert.models;

import cmpt276.project.threatalert.models.Website;
import cmpt276.project.threatalert.models.WebsiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WebsiteRepositoryTest {

    @Mock
    private WebsiteRepository websiteRepository;

    @InjectMocks
    private WebsiteRepositoryTest websiteRepositoryTest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByLink() {
        String link = "http://example.com";
        Website website = new Website(1, link, "Clean");
        List<Website> expectedWebsites = new ArrayList<>();
        expectedWebsites.add(website);

        when(websiteRepository.findByLink(link)).thenReturn(expectedWebsites);

        List<Website> foundWebsites = websiteRepository.findByLink(link);

        assertEquals(1, foundWebsites.size());
        assertEquals(link, foundWebsites.get(0).getLink());
        assertEquals("Clean", foundWebsites.get(0).getThreatlevel());
    }
}
