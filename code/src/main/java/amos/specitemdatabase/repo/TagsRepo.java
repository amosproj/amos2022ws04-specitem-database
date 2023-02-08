package amos.specitemdatabase.repo;

import amos.specitemdatabase.model.TagInfo;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepo extends JpaRepository<TagInfo, String> {
    TagInfo getByShortNameAndCommitTime(final String shortName, final LocalDateTime commitTime);

    TagInfo findFirstByShortNameOrderByCommitTimeDesc(final String specItemId);


}
