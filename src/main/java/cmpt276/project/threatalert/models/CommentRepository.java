package cmpt276.project.threatalert.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByOrderByDateDesc();
}