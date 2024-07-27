package cmpt276.project.threatalert.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ScanRepository extends JpaRepository<Scan, Integer> {

    List<Scan> findBySid(int sid);
    
}
