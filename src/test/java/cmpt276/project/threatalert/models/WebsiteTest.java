package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class WebsiteTest {

    @Test
    public void testDefaultConstructor() {
        Website website = new Website();
        assertEquals(0, website.getWid());
        assertNull(website.getLink());
        assertNull(website.getThreatlevel());
        assertEquals(0, website.getMalicious());
        assertEquals(0, website.getSuspicious());
        assertEquals(0, website.getUndetected());
        assertEquals(0, website.getHarmless());
        assertEquals(0, website.getTimeout());
        assertNull(website.getScans());
    }

    @Test
    public void testConstructorWithLinkAndThreatlevel() {
        String link = "http://example.com";
        String threatlevel = "Clean";
        Website website = new Website(link, threatlevel);

        assertEquals(link, website.getLink());
        assertEquals(threatlevel, website.getThreatlevel());
        assertEquals(0, website.getMalicious());
        assertEquals(0, website.getSuspicious());
        assertEquals(0, website.getUndetected());
        assertEquals(0, website.getHarmless());
        assertEquals(0, website.getTimeout());
        assertNull(website.getScans());
    }

    @Test
    public void testConstructorWithParameters() {
        String link = "http://example.com";
        int malicious = 3;
        int suspicious = 4;
        int undetected = 0;
        int harmless = 2;
        int timeout = 1;

        Website website = new Website(link, malicious, suspicious, undetected, harmless, timeout);

        assertEquals(link, website.getLink());
        assertEquals(malicious, website.getMalicious());
        assertEquals(suspicious, website.getSuspicious());
        assertEquals(undetected, website.getUndetected());
        assertEquals(harmless, website.getHarmless());
        assertEquals(timeout, website.getTimeout());
        assertEquals("Warning!", website.getThreatlevel());
        assertNotNull(website.getScans());
        assertTrue(website.getScans().isEmpty());
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
    public void testSetAndGetMalicious() {
        Website website = new Website();
        int malicious = 5;
        website.setMalicious(malicious);
        assertEquals(malicious, website.getMalicious());
    }

    @Test
    public void testSetAndGetSuspicious() {
        Website website = new Website();
        int suspicious = 3;
        website.setSuspicious(suspicious);
        assertEquals(suspicious, website.getSuspicious());
    }

    @Test
    public void testSetAndGetUndetected() {
        Website website = new Website();
        int undetected = 4;
        website.setUndetected(undetected);
        assertEquals(undetected, website.getUndetected());
    }

    @Test
    public void testSetAndGetHarmless() {
        Website website = new Website();
        int harmless = 2;
        website.setHarmless(harmless);
        assertEquals(harmless, website.getHarmless());
    }

    @Test
    public void testSetAndGetTimeout() {
        Website website = new Website();
        int timeout = 1;
        website.setTimeout(timeout);
        assertEquals(timeout, website.getTimeout());
    }

    @Test
    public void testSetAndGetScans() {
        Website website = new Website();
        List<Scan> scans = new ArrayList<>();
        Scan scan = new Scan();
        scans.add(scan);
        website.setScans(scans);
        assertEquals(scans, website.getScans());
    }

    @Test
    public void testAddScan() {
        Website website = new Website();
        Scan scan = new Scan();
        website.addScan(scan);
        assertTrue(website.getScans().contains(scan));
    }

    @Test
    public void testRemoveScan() {
        Website website = new Website();
        Scan scan = new Scan();
        website.addScan(scan);
        website.removeScan(scan);
        assertFalse(website.getScans().contains(scan));
    }
}