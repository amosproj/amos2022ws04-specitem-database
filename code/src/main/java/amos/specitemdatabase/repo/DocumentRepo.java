package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.DocumentEntity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepo extends JpaRepository<DocumentEntity, Long> {
    @Query(
        value = "SELECT * " +
                "FROM document_entity d1 " +
                "WHERE d1.id = :id",
        nativeQuery = true
    )
    DocumentEntity getDocumentEntityByID(@Param("id") BigInteger ID);

    @Query(
        value = "SELECT document_entity_id " +
                "FROM document_entity_spec_items d1 " +
                "WHERE d1.spec_items_short_name = :short_name " +
                "AND d1.spec_items_time = :time",
        nativeQuery = true
    )
    BigInteger getDocumentEntityIDBySpecItem(@Param("short_name")String ID, @Param("time")LocalDateTime time);

    @Modifying
    @Query(
        value = "DELETE FROM document_entity_spec_items d1 " +
                "WHERE d1.spec_items_short_name = :short_name",
        nativeQuery = true
    )
    void deleteSpecItemByIDFromDocument(@Param("short_name") String ID);

    @Query(
        value = "SELECT spec_items_time " +
                "FROM document_entity_spec_items d1 " +
                "WHERE d1.spec_items_short_name = :short_name",
        nativeQuery = true
    )
    LocalDateTime getLocalDateTimeForSpecItemInsertedViaDocument(@Param("short_name")String ID);
}
