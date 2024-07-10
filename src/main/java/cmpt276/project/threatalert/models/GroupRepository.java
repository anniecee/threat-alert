package cmpt276.project.threatalert.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    
    List<Group> findByGid(int gid);
    List<Group> findByName(String name);
    
}
