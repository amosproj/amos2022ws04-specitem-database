package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import java.time.LocalDateTime;

public interface TagService {

    String fetchTags(final SpecItem specItem);

    void saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime, final String tags);

    boolean saveTagsWithNewCommitTime(final String specItemShortName,
                                      final LocalDateTime newCommitTime, final String allTags);

    TagInfo getLatestById(final String specItemId);

    TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemShortName, final LocalDateTime commitTime);

}
