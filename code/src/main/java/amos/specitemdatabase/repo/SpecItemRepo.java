package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecItemRepo extends JpaRepository<SpecItem, SpecItemId> {
    @Query(
            value = "SELECT * " +
                "FROM spec_item s1 " +
                "WHERE s1.time = (SELECT MAX(s2.time) FROM spec_item s2 WHERE s1.short_name = s2.short_name)",
            countQuery = "SELECT count(*) FROM spec_item",
            nativeQuery = true
    )
    List<SpecItem> findAllUpdatedSpecitem(Pageable pageable);

    @Query(
            value = "SELECT * " +
                    "FROM spec_item s1 " +
                    "WHERE s1.time = (SELECT MAX(s2.time) FROM spec_item s2 WHERE s1.short_name = s2.short_name) " +
                    "AND s1.content LIKE '%'|| ?1 || '%'",
            countQuery = "SELECT count(*) FROM spec_item",
            nativeQuery = true
    )
    List<SpecItem> findUpdatedSpecItemByContent(String content, Pageable pageable);

    @Query(
            value = "SELECT count(*) " +
                    "FROM spec_item s1 " +
                    "WHERE s1.time = (SELECT MAX(s2.time) FROM spec_item s2 WHERE s1.short_name = s2.short_name)",
            nativeQuery = true
    )
    int getCount();
}
