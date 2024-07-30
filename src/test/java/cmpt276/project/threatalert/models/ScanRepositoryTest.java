package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScanRepositoryTest {

    @Mock
    private ScanRepository scanRepository;

    private Website website;
    private User user;
    private Scan scan;

    // Set up the test data before each test
    @BeforeEach
    public void setUp() {
        website = new Website();
        user = new User();
        scan = new Scan(website);
        scan.setUser(user);
        scan.setScanDate(new Date());
        scan.setBookmark(false);
        scan.setToDelete(false);
    }

    // Test saving a Scan object and finding it by SID
    @Test
    public void testSaveAndFindBySid() {
        when(scanRepository.save(any(Scan.class))).thenReturn(scan);
        when(scanRepository.findBySid(scan.getSid())).thenReturn(List.of(scan));

        Scan savedScan = scanRepository.save(scan);
        List<Scan> foundScans = scanRepository.findBySid(savedScan.getSid());

        assertFalse(foundScans.isEmpty());
        assertEquals(1, foundScans.size());
        assertEquals(scan.getSid(), foundScans.get(0).getSid());
        assertEquals(scan.getScanDate(), foundScans.get(0).getScanDate());
        assertEquals(scan.isBookmark(), foundScans.get(0).isBookmark());
        assertEquals(scan.isToDelete(), foundScans.get(0).isToDelete());
        assertEquals(scan.getUser(), foundScans.get(0).getUser());
        assertEquals(scan.getWebsite(), foundScans.get(0).getWebsite());

        verify(scanRepository, times(1)).save(scan);
        verify(scanRepository, times(1)).findBySid(scan.getSid());
    }

    // Test finding a Scan object by a non-existent SID
    @Test
    public void testFindByNonExistentSid() {
        when(scanRepository.findBySid(999)).thenReturn(List.of());

        List<Scan> foundScans = scanRepository.findBySid(999);

        assertTrue(foundScans.isEmpty());

        verify(scanRepository, times(1)).findBySid(999);
    }
}
