package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemId;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface SpecItemRepo extends JpaRepository<SpecItem, SpecItemId> {
    @Query(
            value = "SELECT * " +
                "FROM spec_item s1 " +
                "WHERE s1.commit_time = (SELECT MAX(s2.commit_time) FROM spec_item s2 WHERE s1.short_name = s2.short_name)",
            countQuery = "SELECT count(*) FROM spec_item",
            nativeQuery = true
    )
    List<SpecItem> findAllUpdatedSpecitem(Pageable pageable);

    @Query(
            value = "SELECT * " +
                    "FROM spec_item s1 " +
                    "WHERE s1.commit_time = (SELECT MAX(s2.commit_time) FROM spec_item s2 WHERE s1.short_name = s2.short_name) " +
                    "AND s1.content LIKE '%'|| ?1 || '%'",
            countQuery = "SELECT count(*) FROM spec_item",
            nativeQuery = true
    )
    List<SpecItem> findUpdatedSpecItemByContent(String content, Pageable pageable);

    @Query(
            value = "SELECT count(*) " +
                    "FROM spec_item s1 " +
                    "WHERE s1.commit_time = (SELECT MAX(s2.commit_time) FROM spec_item s2 WHERE s1.short_name = s2.short_name)",
            nativeQuery = true
    )
    int getCount();

    @Query(
        value = "SELECT * " +
                "FROM spec_item s1 " + 
                "WHERE s1.commit_time = (SELECT MAX(s2.commit_time) FROM spec_item s2 " +
                "WHERE s1.short_name = s2.short_name)" +
                "AND s1.short_name = :short_name",
        nativeQuery = true
    )
    SpecItem getLatestSpecItemByID(@Param("short_name") String ID);

    @Query(
        value = "SELECT * " +
                "FROM spec_item " +
                "WHERE short_name = :short_name",
        nativeQuery = true
    )
    List<SpecItem> getAllVersionsOfASpecItemByID(@Param("short_name") String ID);

    List<SpecItem> findAllByShortNameAndContentContaining(String shortName, String Content);

}
