package cmpt276.project.threatalert.models;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebsiteRepository extends JpaRepository<Website, Integer> {

    List<Website> findByLink(String link);

}
