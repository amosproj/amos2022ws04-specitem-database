package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import java.time.LocalDateTime;

public interface TagService {

    String fetchTags(final SpecItem specItem);

    boolean saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime, final String tags);

    TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemId, final LocalDateTime commitTime);
}
