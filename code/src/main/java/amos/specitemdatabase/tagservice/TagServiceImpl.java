package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.TagsRepo;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    @PersistenceContext
    private EntityManager entityManager;

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

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Override
//    public TagInfo saveTags(final String specItemShortName, final LocalDateTime specItemCommitTime,
//                            final String tags, boolean isLockingScenario) {
//        final TagInfo existingTagInfo = this.getLatestById(specItemShortName);
//        String allTags;
//        if (existingTagInfo != null) {
//            if (existingTagInfo.getTags().isEmpty()) {
//                allTags = tags;
//            } else {
//                if (existingTagInfo.getTags().length() > tags.length()) {
//                    allTags = tags;
//                } else {
//                    allTags = existingTagInfo.getTags() + ", " + tags;
//                }
//            }
//            allTags = removeDuplicates(allTags);
//            existingTagInfo.setTags(allTags);
//            return handleSaveAccordingToLockingScenario(specItemCommitTime, isLockingScenario, existingTagInfo);
//        } else {
//            final TagInfo newTagInfo = new TagInfo();
//            newTagInfo.setCommitTime(specItemCommitTime);
//            newTagInfo.setShortName(specItemShortName);
//            newTagInfo.setTags(tags);
//            this.tagsRepo.saveAndFlush(newTagInfo);
//            return handleSaveAccordingToLockingScenario(specItemCommitTime, isLockingScenario, newTagInfo);
//        }
//    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public TagInfo saveTagsNoConcurrency(final String specItemShortName, final LocalDateTime newCommitTime,
                                         final String tags) {
        final TagInfo existingTagInfo = this.getLatestById(specItemShortName);
        String allTags = "";
        if (existingTagInfo != null) {
            if (existingTagInfo.getTags().isEmpty()) {
                allTags = tags;
            } else {
                if (existingTagInfo.getTags().length() > tags.length()) {
                    allTags = tags;
                } else {
                    allTags = existingTagInfo.getTags() + ", " + tags;
                }
            }
        }
        allTags = removeDuplicates(allTags);
        final TagInfo newTagInfo = new TagInfo();
        newTagInfo.setShortName(specItemShortName);
        newTagInfo.setCommitTime(newCommitTime);
        newTagInfo.setTags(allTags);
        return this.tagsRepo.saveAndFlush(newTagInfo);
    }

    private String removeDuplicates(final String input) {
        final Set<String> set = new HashSet<>(Arrays.asList(input.split(", ")));
        return String.join(", ", set);
    }


    private TagInfo handleSaveAccordingToLockingScenario(final LocalDateTime specItemCommitTime, final boolean isLockingScenario,
                                                         final TagInfo newTagInfo) {
        if (isLockingScenario) {
            log.info("Saving the spec item as a res of res of locking. Commit time: {}", specItemCommitTime);
            return this.entityManager.merge(newTagInfo);
        } else {
            log.info("Saving the spec item normally. Commit time: {}", specItemCommitTime);
            return this.tagsRepo.saveAndFlush(newTagInfo);
        }
    }

    @Override
    public TagInfo getLatestById(final String specItemId) {
        return this.tagsRepo.findFirstByShortNameOrderByCommitTimeDesc(specItemId);
    }

    @Override
    public TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemShortName, final LocalDateTime commitTime) {
        return this.tagsRepo.getByShortNameAndCommitTime(specItemShortName, commitTime);
    }
}
