package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import java.time.LocalDateTime;

public interface TagService {

    String fetchTags(final SpecItem specItem);

//    TagInfo saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime,
//                  final String tags, final boolean fromLocking);

    TagInfo saveTagsNoConcurrency(final String specItemShortName, final LocalDateTime specItemCommitTime,
                                  final String tags);

    TagInfo getLatestById(final String specItemId);

    TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemShortName, final LocalDateTime commitTime);

}
