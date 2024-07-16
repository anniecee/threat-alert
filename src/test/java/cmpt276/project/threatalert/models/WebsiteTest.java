package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class WebsiteTest {

    @Test
    public void testDefaultConstructor() {
        // Tests the default constructor of the Website class
        Website website = new Website();
        assertEquals(0, website.getWid());
        assertNull(website.getLink());
        assertNull(website.getThreatlevel());
        assertNull(website.getDate());
        assertNull(website.getUser());
    }

    @Test
    public void testConstructorWithParameters() {
        // Tests the parameterized constructor of the Website class
        String link = "http://example.com";
        String threatlevel = "Clean";
        Website website = new Website(link, threatlevel);

        assertEquals(link, website.getLink());
        assertEquals(threatlevel, website.getThreatlevel());
        assertNotNull(website.getDate());
        assertNull(website.getUser());
    }

    @Test
    public void testSetAndGetWid() {
        // Tests the setter and getter for the wid field
        Website website = new Website();
        int wid = 1;
        website.setWid(wid);
        assertEquals(wid, website.getWid());
    }

    @Test
    public void testSetAndGetLink() {
        // Tests the setter and getter for the link field
        Website website = new Website();
        String link = "http://example.com";
        website.setLink(link);
        assertEquals(link, website.getLink());
    }

    @Test
    public void testSetAndGetThreatlevel() {
        // Tests the setter and getter for the threatlevel field
        Website website = new Website();
        String threatlevel = "High";
        website.setThreatlevel(threatlevel);
        assertEquals(threatlevel, website.getThreatlevel());
    }

    @Test
    public void testSetAndGetDate() {
        // Tests the setter and getter for the date field
        Website website = new Website();
        Date date = new Date();
        website.setDate(date);
        assertEquals(date, website.getDate());
    }

    @Test
    public void testSetAndGetUser() {
        // Tests the setter and getter for the user field
        Website website = new Website();
        User user = new User("user@example.com", "password");
        website.setUser(user);
        assertEquals(user, website.getUser());
    }
}
