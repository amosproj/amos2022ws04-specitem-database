package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<DocumentEntity, Long> {
}
