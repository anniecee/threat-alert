package cmpt276.project.threatalert.models;

import cmpt276.project.threatalert.models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getType());
        assertEquals(0, user.getUid());
    }

    @Test
    public void testConstructorForRegularUser() {
        String email = "test@example.com";
        String password = "password123";
        User user = new User(email, password);
        
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals("regular", user.getType());
    }

    @Test
    public void testConstructorForNonRegularUser() {
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
        User user = new User();
        int uid = 1;
        user.setUid(uid);
        assertEquals(uid, user.getUid());
    }

    @Test
    public void testSetAndGetEmail() {
        User user = new User();
        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetAndGetPassword() {
        User user = new User();
        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetAndGetType() {
        User user = new User();
        String type = "admin";
        user.setType(type);
        assertEquals(type, user.getType());
    }
}
