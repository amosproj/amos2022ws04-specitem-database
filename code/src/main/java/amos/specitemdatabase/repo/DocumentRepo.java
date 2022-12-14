package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentEntity, Long> {
    @Query(
        value = "SELECT * " +
                "FROM document_entity " +
                "WHERE document_name = :document_name",
        nativeQuery = true
    )
    DocumentEntity getDocumentEntityByID(@Param("document_name") String ID);
}
