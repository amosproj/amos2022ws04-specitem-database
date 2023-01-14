package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.tagservice.TagService;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
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

    private String previousTags = "previousKey:previousValue";
    private String newTagsUser1 = "key1:value1, key2:value2";
    private String newTagsUser2 = "key3:value3, key4:value4";

    @Test
    void testTagsAdditionNoConcurrency() throws IOException {
        final SpecItem specItem = this.createSpecItem();

    }

    private SpecItem createSpecItem() throws IOException {
        // Step 1: newly created spec items have no tags
        this.specItemService.saveDocument("simpleSpecItem.txt");
        final SpecItem specItemNoTags = this.specItemRepo.findAll().get(0);
        // Step 2: Add some tags
        this.specItemService.saveTags(specItemNoTags, List.of(previousTags));
        // Step 3: Get the spec item
        return this.specItemRepo.getLatestSpecItemByID(specItemNoTags.getShortName());
    }

}
