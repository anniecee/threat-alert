package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testDefaultConstructor() {
        // Tests the default constructor of the User class
        User user = new User();
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getType());
        assertEquals(0, user.getUid());
    }

    @Test
    public void testConstructorForRegularUser() {
        // Tests the constructor for a regular user
        String email = "test@example.com";
        String password = "password123";
        User user = new User(email, password);
        
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals("regular", user.getType());
    }

    @Test
    public void testConstructorForNonRegularUser() {
        // Tests the constructor for a non-regular user (e.g., admin)
        String email = "admin@example.com";
        String password = "admin123";
        String type = "admin";
        User user = new User(email, password, type);
        
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(type, user.getType());
    }

    @Test
    public void testSetAndGetUid() {
        // Tests the setter and getter for the uid field
        User user = new User();
        int uid = 1;
        user.setUid(uid);
        assertEquals(uid, user.getUid());
    }

    @Test
    public void testSetAndGetEmail() {
        // Tests the setter and getter for the email field
        User user = new User();
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        // Tests the setter and getter for the password field
        User user = new User();
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetAndGetType() {
        // Tests the setter and getter for the type field
        User user = new User();
        String type = "admin";
        user.setType(type);
        assertEquals(type, user.getType());
    }
}
