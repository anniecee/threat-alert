package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryTest userRepositoryTest;

    @BeforeEach
    public void setUp() {
        // Initializes mock objects before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByEmailAndPassword() {
        // Tests the findByEmailAndPassword method of the UserRepository
        String name = "joe";
        String email = "testuser@example.com";
        String password = "password123";
        User user = new User(name, email, password);

        when(userRepository.findByEmailAndPassword(email, password)).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByEmailAndPassword(email, password);

        assertEquals(1, foundUsers.size());
        assertEquals(email, foundUsers.get(0).getEmail());
        assertEquals(password, foundUsers.get(0).getPassword());
    }

    @Test
    public void testFindByEmailAndPasswordFail() {
        // Tests the findByEmailAndPassword method of the UserRepository
        // Test when combination not found
        String name = "joe";
        String email = "testuser@example.com";
        String password = "password123";
        User user = new User(name, email, password);

        when(userRepository.findByEmailAndPassword("random@gmail.com", password)).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByEmailAndPassword("random@gmail.com", password);

        assertEquals(0, foundUsers.size());
    }

    @Test
    public void testFindByEmail() {
        // Tests the findByEmail method of the UserRepository
        String name = "joe";
        String email = "testuser@example.com";
        String password = "password123";
        User user = new User(name, email, password);

        when(userRepository.findByEmail(email)).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByEmail(email);

        assertEquals(1, foundUsers.size());
        assertEquals(email, foundUsers.get(0).getEmail());
    }

    @Test
    public void testFindByUid() {
        // Tests the findByUid method of the UserRepository
        int uid = 1;
        User user = new User();
        user.setUid(uid);

        when(userRepository.findByUid(uid)).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByUid(uid);

        assertEquals(1, foundUsers.size());
        assertEquals(uid, foundUsers.get(0).getUid());
    }
}
