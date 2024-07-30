package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ScanTest {

    private Scan scan;
    private Website website;
    private User user;

    @BeforeEach
    public void setUp() {
        website = new Website();
        user = new User();
        scan = new Scan(website);
    }

    @Test
    public void testDefaultConstructor() {
        Scan scan = new Scan();
        assertNotNull(scan);
        assertNull(scan.getScanDate());
        assertFalse(scan.isBookmark());
        assertFalse(scan.isToDelete());
        assertNull(scan.getUser());
        assertNull(scan.getWebsite());
    }

    @Test
    public void testConstructorWithWebsite() {
        assertNotNull(scan);
        assertNotNull(scan.getScanDate());
        assertFalse(scan.isBookmark());
        assertFalse(scan.isToDelete());
        assertNull(scan.getUser());
        assertEquals(website, scan.getWebsite());
    }

    @Test
    public void testGettersAndSetters() {
        Date now = new Date();
        scan.setSid(1);
        scan.setScanDate(now);
        scan.setBookmark(true);
        scan.setBookmarkDate(now);
        scan.setToDelete(true);
        scan.setUser(user);
        scan.setWebsite(website);

        assertEquals(1, scan.getSid());
        assertEquals(now, scan.getScanDate());
        assertTrue(scan.isBookmark());
        assertEquals(now, scan.getBookmarkDate());
        assertTrue(scan.isToDelete());
        assertEquals(user, scan.getUser());
        assertEquals(website, scan.getWebsite());
    }

    @Test
    public void testSetSid() {
        scan.setSid(1);
        assertEquals(1, scan.getSid());
    }

    @Test
    public void testSetScanDate() {
        Date now = new Date();
        scan.setScanDate(now);
        assertEquals(now, scan.getScanDate());
    }

    @Test
    public void testSetBookmark() {
        scan.setBookmark(true);
        assertTrue(scan.isBookmark());
    }

    @Test
    public void testSetBookmarkDate() {
        Date now = new Date();
        scan.setBookmarkDate(now);
        assertEquals(now, scan.getBookmarkDate());
    }

    @Test
    public void testSetToDelete() {
        scan.setToDelete(true);
        assertTrue(scan.isToDelete());
    }

    @Test
    public void testSetUser() {
        scan.setUser(user);
        assertEquals(user, scan.getUser());
    }

    @Test
    public void testSetWebsite() {
        scan.setWebsite(website);
        assertEquals(website, scan.getWebsite());
    }
}
