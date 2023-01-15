package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.TagInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepo extends JpaRepository<TagInfo, String> {

    @Query(value =  "SELECT t FROM TagInfo t " +
                    "WHERE t.status = amos.specitemdatabase.model.Status.LATEST " +
                    "AND t.shortName = :shortName")
    List<TagInfo> getLatestTagInfo(@Param("shortName") String shortName);

    @Modifying
    @Query(value = "UPDATE TagInfo t SET t.tags = :tags " +
        "WHERE t.shortName = :specItemShortName" +
        " AND t.time = :specItemCommitTime")
    void updateTags(@Param("specItemShortName") final String specItemShortName,
                    @Param("specItemCommitTime") final LocalDateTime specItemCommitTime,
                    @Param("tags") final String tags);
}
