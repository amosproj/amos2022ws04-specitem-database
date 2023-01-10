package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.TagInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepo extends JpaRepository<TagInfo, String> {

    @Query(value =  "SELECT t FROM TagInfo t " +
                    "WHERE t.status = amos.specitemdatabase.model.Status.LATEST " +
                    "AND t.shortName = :shortName")
    List<TagInfo> getLatestTagInfo(@Param("shortName") String shortName);
}
