package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Date date;

    @BeforeEach
    public void setUp() {
        user = new User("Test User", "test@example.com", "password");
    }

    @Test
    public void testDefaultConstructor() {
        // Test default constructor
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
        // Test parameterized constructor
        assertEquals("Test User", user.getName());
        assertEquals(date, user.getDate());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("regular", user.getType());
        assertNotNull(user.getScans());
    }

    @Test
    public void testAdminConstructor() {
        // Test admin constructor
        User adminUser = new User("admin@example.com", "adminpassword", "admin");
        assertEquals("Admin", adminUser.getName());
        assertNotNull(adminUser.getDate());
        assertEquals("admin@example.com", adminUser.getEmail());
        assertEquals("adminpassword", adminUser.getPassword());
        assertEquals("admin", adminUser.getType());
        assertNotNull(adminUser.getScans());
    }

    @Test
    public void testGetAndSetUid() {
        // Test getter and setter for uid
        user.setUid(1);
        assertEquals(1, user.getUid());
    }

    @Test
    public void testGetAndSetEmail() {
        // Test getter and setter for email
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    public void testGetAndSetPassword() {
        // Test getter and setter for password
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    public void testGetAndSetType() {
        // Test getter and setter for type
        user.setType("admin");
        assertEquals("admin", user.getType());
    }

    @Test
    public void testGetAndSetName() {
        // Test getter and setter for name
        user.setName("New Name");
        assertEquals("New Name", user.getName());
    }

    @Test
    public void testGetAndSetDate() {
        // Test getter and setter for date
        Date newDate = new Date();
        user.setDate(newDate);
        assertEquals(newDate, user.getDate());
    }

    @Test
    public void testGetAndSetScans() {
        // Test getter and setter for scans
        Scan scan = new Scan();
        user.setScans(List.of(scan));
        assertNotNull(user.getScans());
        assertEquals(1, user.getScans().size());
        assertEquals(scan, user.getScans().get(0));
    }

    @Test
    public void testAddScan() {
        // Test addScan method
        Scan scan = new Scan();
        user.addScan(scan);
        assertNotNull(user.getScans());
        assertEquals(1, user.getScans().size());
        assertEquals(scan, user.getScans().get(0));
    }

    @Test
    public void testRemoveScan() {
        // Test removeScan method
        Scan scan = new Scan();
        user.addScan(scan);
        assertEquals(1, user.getScans().size());
        user.removeScan(scan);
        assertEquals(0, user.getScans().size());
    }
}