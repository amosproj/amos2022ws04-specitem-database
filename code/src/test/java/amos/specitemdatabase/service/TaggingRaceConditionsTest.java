package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.repo.TagsRepo;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaggingRaceConditionsTest {

    @Autowired
    private SpecItemService specItemService;

    @Autowired
    private SpecItemRepo specItemRepo;

    @Autowired
    private TagsRepo tagsRepo;

    private final String originalTag = "previousKey:previousValue";
    private final String newTagsUser1 = "key1:value1,key2:value2";
    private final String newTagsUser2 = "key3:value3,key4:value4";

    @Transactional
    @Test
    void testTagsAdditionNoConcurrency() throws IOException, InterruptedException {
        final SpecItem specItem = this.createSpecItemWithTag();
        // Creating a new spec item with the tag shall also make the first version of the tag
        final Long originalTagVersion = specItem.getTagInfo().getVersion();
        //Assertions.assertEquals(1, originalTagVersion);
        // Make two subsequent updates of the tag
        this.specItemService.completeTagAdditionProcess(specItem, Collections.singletonList(newTagsUser1));
        Thread.sleep(5000);
        this.specItemService.completeTagAdditionProcess(specItem, Collections.singletonList(newTagsUser2));
        final List<TagInfo> tags = this.tagsRepo.findAll();
        //final TagInfo tagInfo = this.tagService.getTagsBySpecItemIdAndCommitTime(specItemShortName, specItemCommitTime);
        final String expectedAllTags = String.join(",", List.of(originalTag, newTagsUser1, newTagsUser2));
        Assertions.assertEquals(expectedAllTags, tags.get(0).getTags());
        Assertions.assertEquals(3, tags.get(0).getVersion());
    }

    @Test
    void testTagsAdditionWithConcurrency() throws InterruptedException, IOException {
        // same setup as above
        final SpecItem specItem = this.createSpecItemWithTag();
        // Creating a new spec item with the tag shall also make the first version of the tag
        final Long originalTagVersion = specItem.getTagInfo().getVersion();
        Assertions.assertEquals(1, originalTagVersion);
        // Now, add the tags using threads
        final ExecutorService executor = Executors.newFixedThreadPool(3);
        String newTagsUser3 = "key5:value5,key6:value6";
        final List<String> allTags = List.of(newTagsUser1, newTagsUser2, newTagsUser3);
        for (int i = 0; i < 3 ; i++) {
            final int finalI = i;
            executor.execute(() ->
                this.specItemService.completeTagAdditionProcess(specItem,
                    Collections.singletonList(allTags.get(finalI))));
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        final int expectedAllTagsLength = String.join(",", List.of(originalTag, newTagsUser1,
            newTagsUser2, newTagsUser3)).length();
        final List<TagInfo> tags = this.tagsRepo.findAll();
        Assertions.assertEquals(expectedAllTagsLength, tags.get(0).getTags().length());
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
