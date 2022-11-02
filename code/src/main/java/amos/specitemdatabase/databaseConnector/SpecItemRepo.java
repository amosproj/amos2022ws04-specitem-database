package amos.specitemdatabase.databaseConnector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecItemRepo extends JpaRepository<SpecItem, Long> {
}
