package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.TagsRepo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
        final TagInfo tagInfos = this.tagsRepo.findFirstByShortNameOrderByCommitTimeDesc(specItem.getShortName());
        return tagInfos != null ? tagInfos.getTags() : "";
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime,
                            final String tags) {
        final TagInfo existingTagInfo = this.tagsRepo.getByShortNameAndCommitTime(specItemShortName, specItemCommitTime);
        if (existingTagInfo == null) {
            log.info("No previous entry for the SpecItem with {} and {}", specItemShortName, specItemCommitTime);
            this.prepareNewTagInfo(specItemShortName, specItemCommitTime, tags);
        } else {
            log.info("There are some tags for the SpecItem with {} and {}", specItemShortName, specItemCommitTime);
            String allTags = existingTagInfo.getTags() + ", " + tags;
            existingTagInfo.setTags(allTags);
            this.tagsRepo.save(existingTagInfo);
        }
    }

    private boolean prepareNewTagInfo(final String specItemShortName, final LocalDateTime specItemCommitTime,
                                      final String tags) {
        final TagInfo tagInfo = new TagInfo();
        tagInfo.setTags(tags);
        tagInfo.setShortName(specItemShortName);
        tagInfo.setCommitTime(specItemCommitTime);
        final TagInfo saved = this.tagsRepo.save(tagInfo);
        final boolean isSaved = saved.getTags().contains(tags);
        log.info("SaveTags method: Do the saved tags contain the provided tags: {}", isSaved);
        return isSaved;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean saveTagsWithNewCommitTime(final String specItemShortName, final LocalDateTime newCommitTime,
                                             final String tags) {

        return prepareNewTagInfo(specItemShortName, newCommitTime, tags, tags);
    }

    @Override
    public TagInfo getLatestById(final String specItemId) {
        return this.tagsRepo.findFirstByShortNameOrderByCommitTimeDesc(specItemId);
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
