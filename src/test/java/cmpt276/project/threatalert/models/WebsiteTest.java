package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class WebsiteTest {

    @Test
    public void testDefaultConstructor() {
        Website website = new Website();
        assertEquals(0, website.getWid());
        assertNull(website.getLink());
        assertNull(website.getThreatlevel());
        assertNull(website.getDate());
    }

    @Test
    public void testConstructorWithParameters() {
        int wid = 1;
        String link = "http://example.com";
        String threatlevel = "Clean";
        Website website = new Website( link, threatlevel);

        assertEquals(wid, website.getWid());
        assertEquals(link, website.getLink());
        assertEquals(threatlevel, website.getThreatlevel());
        assertNotNull(website.getDate());
    }

    @Test
    public void testSetAndGetWid() {
        Website website = new Website();
        int wid = 1;
        website.setWid(wid);
        assertEquals(wid, website.getWid());
    }

    @Test
    public void testSetAndGetLink() {
        Website website = new Website();
        String link = "http://example.com";
        website.setLink(link);
        assertEquals(link, website.getLink());
    }

    @Test
    public void testSetAndGetThreatlevel() {
        Website website = new Website();
        String threatlevel = "High";
        website.setThreatlevel(threatlevel);
        assertEquals(threatlevel, website.getThreatlevel());
    }

    @Test
    public void testSetAndGetDate() {
        Website website = new Website();
        Date date = new Date();
        website.setDate(date);
        assertEquals(date, website.getDate());
    }
}