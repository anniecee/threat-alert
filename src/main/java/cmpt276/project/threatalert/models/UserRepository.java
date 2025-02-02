package cmpt276.project.threatalert.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
 
    List<User> findByEmailAndPassword(String email, String password);
    List<User> findByEmail(String email);
    List<User> findByUid(int uid);

}
