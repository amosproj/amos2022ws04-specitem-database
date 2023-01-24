package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.TagsRepo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    private final TagsRepo tagsRepo;
    @Autowired
    public TagServiceImpl(final TagsRepo tagsRepo) {
        this.tagsRepo = tagsRepo;
    }

    @Override
    public String fetchTags(final SpecItem specItem) {
        List<TagInfo> tagInfos = this.tagsRepo.getLatestTagInfo(specItem.getShortName());
        if (tagInfos.size() > 1) {
            // TODO: A correct mechanism must be implemented here
            return tagInfos.get(tagInfos.size() -1).getTags();
        } else if (tagInfos.size() == 1) {
            return tagInfos.get(0).getTags();
        } else {
            return "";
        }
    }

    @Override
    public void saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime, final String tags) {
        this.tagsRepo.updateTags(specItemShortName, specItemCommitTime, tags);
    }

    @Override
    public TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemShortName, final LocalDateTime commitTime) {
        return this.tagsRepo.getByShortNameCommitTime(specItemShortName, commitTime);
    }
}
