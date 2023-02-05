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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE TagInfo t SET t.tags = :tags " +
        "WHERE t.shortName = :shortName" +
        " AND t.commitTime = :commitTime")
    void updateTags(@Param("shortName") final String shortName,
                    @Param("commitTime") final LocalDateTime commitTime,
                    @Param("tags") final String tags);


    TagInfo getByShortNameAndCommitTime(final String shortName, final LocalDateTime commitTime);

    TagInfo findFirstByShortNameOrderByCommitTimeDesc(final String specItemId);


}
