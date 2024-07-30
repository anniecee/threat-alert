package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentRepositoryTest {

    @Mock
    private CommentRepository commentRepository;

    private User user;
    private Website website;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUid(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");

        website = new Website();
        website.setWid(1);
        website.setLink("https://example.com");

        comment = new Comment(user, "This is a test comment", new Date(), website);
        comment.setCid(1);
    }

    @Test
    public void testFindByCid() {
        // Test finding a comment by CID
        when(commentRepository.findByCid(1)).thenReturn(List.of(comment));

        List<Comment> foundComments = commentRepository.findByCid(1);

        assertThat(foundComments).hasSize(1);
        assertThat(foundComments.get(0).getContent()).isEqualTo("This is a test comment");

        verify(commentRepository, times(1)).findByCid(1);
    }

    @Test
    public void testFindByOrderByDateDesc() {
        // Test finding comments ordered by date descending
        Comment comment2 = new Comment(user, "This is another test comment", new Date(), website);
        comment2.setCid(2);
        List<Comment> comments = List.of(comment, comment2);
        when(commentRepository.findByOrderByDateDesc()).thenReturn(comments);

        List<Comment> foundComments = commentRepository.findByOrderByDateDesc();

        assertThat(foundComments).hasSize(2);
        assertThat(foundComments.get(0).getContent()).isEqualTo("This is a test comment");
        assertThat(foundComments.get(1).getContent()).isEqualTo("This is another test comment");

        verify(commentRepository, times(1)).findByOrderByDateDesc();
    }
}
