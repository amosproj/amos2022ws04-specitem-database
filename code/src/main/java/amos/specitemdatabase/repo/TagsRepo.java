package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.TagInfo;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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
    @Lock(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
    @Query(value = "SELECT t FROM TagInfo t " +
        "WHERE t.commitTime = :commitTime " +
        "AND t.shortName = :shortName")
    TagInfo getByShortNameCommitTime(@Param("shortName") final String shortName,
                                     @Param("commitTime") final LocalDateTime commitTime);

    @Modifying
    @Query(value = "UPDATE TagInfo t SET t.version = :version" +
        " WHERE t.shortName = :shortName AND t.commitTime = :commitTime")
    void updateVersion(@Param("shortName") final String shortName,
                    @Param("commitTime") final LocalDateTime commitTime,
                       @Param("version") final long version);
}
