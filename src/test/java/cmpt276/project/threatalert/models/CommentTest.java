package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommentTest {

    private Comment comment;
    private User user;
    private Website website;
    private Date date;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUid(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");

        website = new Website();
        website.setWid(1);
        website.setLink("https://example.com");

        date = new Date();
        comment = new Comment(user, "This is a test comment", date, website);
    }

    @Test
    public void testDefaultConstructor() {
        // Tests the default constructor
        Comment newComment = new Comment();
        assertNull(newComment.getContent());
        assertNull(newComment.getDate());
        assertNull(newComment.getUser());
        assertNull(newComment.getWebsite());
    }

    @Test
    public void testParameterizedConstructor() {
        // Tests the parameterized constructor
        assertEquals("This is a test comment", comment.getContent());
        assertEquals(date, comment.getDate());
        assertEquals(user, comment.getUser());
        assertEquals(website, comment.getWebsite());
    }

    @Test
    public void testGettersAndSetters() {
        // Tests the getters and setters
        comment.setCid(1);
        assertEquals(1, comment.getCid());

        comment.setContent("Updated content");
        assertEquals("Updated content", comment.getContent());

        Date newDate = new Date();
        comment.setDate(newDate);
        assertEquals(newDate, comment.getDate());

        User newUser = new User();
        newUser.setUid(2);
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password456");
        comment.setUser(newUser);
        assertEquals(newUser, comment.getUser());

        Website newWebsite = new Website();
        newWebsite.setWid(2);
        newWebsite.setLink("https://newexample.com");
        comment.setWebsite(newWebsite);
        assertEquals(newWebsite, comment.getWebsite());
    }
}
