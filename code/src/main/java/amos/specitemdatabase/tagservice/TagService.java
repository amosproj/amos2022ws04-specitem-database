package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import java.time.LocalDateTime;

public interface TagService {

    String fetchTags(final SpecItem specItem);

    void saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime, final String tags);
}
