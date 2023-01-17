package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.tagservice.TagService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaggingRaceConditionsTest {

    @Autowired
    private SpecItemService specItemService;

    @Autowired
    private SpecItemRepo specItemRepo;

    @SpyBean
    private TagService tagService;

    private final String originalTag = "previousKey:previousValue";
    private final String newTagsUser1 = "key1:value1,key2:value2";
    private final String newTagsUser2 = "key3:value3,key4:value4";

    @Test
    void testTagsAdditionNoConcurrency() throws IOException {
        final SpecItem specItem = this.createSpecItemWithTag();
        final String specItemShortName = specItem.getShortName(); // SpecItem_1
        final LocalDateTime specItemCommitTime = specItem.getCommitTime(); // 2022-12-01 17:30:00
        // Creating a new spec item with the tag shall also make the first version of the tag
        final Long originalTagVersion = specItem.getTagInfo().getVersion();
        Assertions.assertEquals(0, originalTagVersion);
        // Make two subsequent updates of the tag
        this.specItemService.completeTagAdditionProcess(specItem, Collections.singletonList(newTagsUser1));
        this.specItemService.completeTagAdditionProcess(specItem, Collections.singletonList(newTagsUser2));
        //final SpecItem specItemWithAllNewTags = this.specItemRepo.getLatestSpecItemByID(specItem.getShortName());
        final TagInfo tagInfo = this.tagService.getTagsBySpecItemIdAndCommitTime(specItemShortName, specItemCommitTime);
        final String expectedAllTags = String.join(",", List.of(originalTag, newTagsUser1, newTagsUser2));
        Assertions.assertAll(
            () -> Assertions.assertEquals(expectedAllTags, tagInfo.getTags()),
            () -> Mockito.verify(tagService, Mockito.times(8)).fetchTags(Mockito.any())
        );

    }

    private SpecItem createSpecItemWithTag() throws IOException {
        // Step 1: newly created spec items have no tags
        this.specItemService.saveDocument("simpleSpecItem.txt");
        final SpecItem specItemNoTags = this.specItemRepo.findAll().get(0);
        // Step 2: Add some tags
        this.specItemService.completeTagAdditionProcess(specItemNoTags, List.of(originalTag));
        // Step 3: Get the original spec item to simulate the access of the same spec item in the gui
        return this.specItemRepo.findAll().get(0);
    }
}
