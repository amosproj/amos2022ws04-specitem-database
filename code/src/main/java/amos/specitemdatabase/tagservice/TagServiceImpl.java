package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.TagsRepo;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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
    public boolean saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime,
                            final String tags) {

        final String currTags = this.fetchCurrentTags(specItemShortName, specItemCommitTime,
            Collections.singletonList(tags));
        TagInfo tagInfo = new TagInfo();
        tagInfo.setTags(currTags);
        tagInfo.setShortName(specItemShortName);
        tagInfo.setCommitTime(specItemCommitTime);
        TagInfo saved = this.tagsRepo.saveAndFlush(tagInfo);
        return saved.getTags().contains(tags);
    }

    @Override
    public TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemShortName, final LocalDateTime commitTime) {
        return this.tagsRepo.getByShortNameCommitTime(specItemShortName, commitTime);
    }

    private String fetchCurrentTags(final String specItemShortName, final LocalDateTime specItemCommitTime,
                                    final List<String> newTags) {
        final TagInfo previousTagInfo = this.getTagsBySpecItemIdAndCommitTime(
            specItemShortName, specItemCommitTime);
        String allTags;
        if (previousTagInfo != null) {
            final String previousTags = previousTagInfo.getTags();
            log.info("The already existing tags for ID={} CommitTime={} are {}",
                specItemShortName, specItemCommitTime, previousTags);
            if (previousTags.isEmpty()) {
                allTags = String.join(",", newTags);
            } else {
                allTags = previousTags + "," + String.join(",", newTags);
            }
        } else {
            allTags = String.join(",", newTags);
        }
        return allTags;
    }
}
