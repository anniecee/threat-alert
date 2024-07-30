package cmpt276.project.threatalert.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUid(1);
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    // Test finding a User by email and password
    @Test
    public void testFindByEmailAndPassword() {
        when(userRepository.findByEmailAndPassword("test@example.com", "password123")).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByEmailAndPassword("test@example.com", "password123");

        assertFalse(foundUsers.isEmpty());
        assertEquals(1, foundUsers.size());
        assertEquals(user.getUid(), foundUsers.get(0).getUid());
        assertEquals(user.getEmail(), foundUsers.get(0).getEmail());
        assertEquals(user.getPassword(), foundUsers.get(0).getPassword());

        verify(userRepository, times(1)).findByEmailAndPassword("test@example.com", "password123");
    }

    // Test finding a User by email
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
        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByEmail("test@example.com");

        assertFalse(foundUsers.isEmpty());
        assertEquals(1, foundUsers.size());
        assertEquals(user.getUid(), foundUsers.get(0).getUid());
        assertEquals(user.getEmail(), foundUsers.get(0).getEmail());

        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    // Test finding a User by UID
    @Test
    public void testFindByUid() {
        when(userRepository.findByUid(1)).thenReturn(List.of(user));

        List<User> foundUsers = userRepository.findByUid(1);

        assertFalse(foundUsers.isEmpty());
        assertEquals(1, foundUsers.size());
        assertEquals(user.getUid(), foundUsers.get(0).getUid());

        verify(userRepository, times(1)).findByUid(1);
    }

    // Test finding a User by a non-existent email
    @Test
    public void testFindByNonExistentEmail() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(List.of());

        List<User> foundUsers = userRepository.findByEmail("nonexistent@example.com");

        assertTrue(foundUsers.isEmpty());

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com"); 
    }

    // Test finding a User by a non-existent UID
    @Test
    public void testFindByNonExistentUid() {
        when(userRepository.findByUid(999)).thenReturn(List.of());

        List<User> foundUsers = userRepository.findByUid(999);

        assertTrue(foundUsers.isEmpty());

        verify(userRepository, times(1)).findByUid(999);
    }
}