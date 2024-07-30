package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Date date;

    @BeforeEach
    public void setUp() {
        date = new Date();
        user = new User("Test User", "test@example.com", "password");
    }

    @Test
    public void testDefaultConstructor() {
        User defaultUser = new User();
        assertNull(defaultUser.getName());
        assertNull(defaultUser.getDate());
        assertNull(defaultUser.getEmail());
        assertNull(defaultUser.getPassword());
        assertNull(defaultUser.getType());
        assertNull(defaultUser.getScans());
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User("Test User", "test@example.com", "password");
        assertEquals("Test User", user.getName());
        assertNotNull(user.getDate());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("regular", user.getType());
        assertNotNull(user.getScans());
    }

    @Test
    public void testAdminConstructor() {
        User adminUser = new User("Admin", "admin@example.com", "adminpassword", "admin");
        assertEquals("Admin", adminUser.getName());
        assertEquals(date, adminUser.getDate());
        assertEquals("admin@example.com", adminUser.getEmail());
        assertEquals("adminpassword", adminUser.getPassword());
        assertEquals("admin", adminUser.getType());
        assertNotNull(adminUser.getScans());
    }

    @Test
    public void testGetAndSetUid() {
        user.setUid(1);
        assertEquals(1, user.getUid());
    }

    @Test
    public void testGetAndSetEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void testGetAndSetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testGetAndSetType() {
        user.setType("admin");
        assertEquals("admin", user.getType());
    }

    @Test
    public void testGetAndSetName() {
        user.setName("New Name");
        assertEquals("New Name", user.getName());
    }

    @Test
    public void testGetAndSetDate() {
        Date newDate = new Date();
        user.setDate(newDate);
        assertEquals(newDate, user.getDate());
    }

    @Test
    public void testGetAndSetScans() {
        Scan scan = new Scan();
        user.setScans(List.of(scan));
        assertNotNull(user.getScans());
        assertEquals(1, user.getScans().size());
        assertEquals(scan, user.getScans().get(0));
    }

    @Test
    public void testAddScan() {
        Scan scan = new Scan();
        user.addScan(scan);
        assertNotNull(user.getScans());
        assertEquals(1, user.getScans().size());
        assertEquals(scan, user.getScans().get(0));
    }

    @Test
    public void testRemoveScan() {
        Scan scan = new Scan();
        user.addScan(scan);
        assertEquals(1, user.getScans().size());
        user.removeScan(scan);
        assertEquals(0, user.getScans().size());
    }

    @Test
    public void addAndRemoveMultipleScans() {
        // Test addScan and removeScan multiple times
        int qty = 5;
        List<Scan> scans = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            Scan scan = new Scan();
            scan.setSid(i);
            scans.add(scan);
            user.addScan(scan);

            assertEquals(i, user.getScans().get(i).getSid());
        }

        for (int i = 0; i < qty; i++) {
            user.removeScan(scans.get(0));
            scans.remove(0);
            assertEquals(--qty, user.getScans().size());
        }
    }
}