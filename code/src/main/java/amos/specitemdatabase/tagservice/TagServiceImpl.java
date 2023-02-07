package amos.specitemdatabase.tagservice;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.TagsRepo;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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

    @Override
    public TagInfo getLatestById(final String specItemId) {
        return this.tagsRepo.findFirstByShortNameOrderByCommitTimeDesc(specItemId);
    }

    @Override
    public TagInfo getTagsBySpecItemIdAndCommitTime(final String specItemShortName, final LocalDateTime commitTime) {
        return this.tagsRepo.getByShortNameAndCommitTime(specItemShortName, commitTime);
    }
}
