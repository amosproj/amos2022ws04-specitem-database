package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.repo.SpecItemRepo;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TaggingMechanismTest {

    @Autowired
    private SpecItemService specItemService;

    @Autowired
    private SpecItemRepo specItemRepo;

    @Test
    void shouldAddTagsToUpdatedSpecItems() throws IOException {

        // Step 1: newly created spec items have no tags
        this.specItemService.saveDocument("file1.txt");
        // There should be 4 spec items in the DB
        final List<SpecItem> specItems = this.specItemRepo.findAll();
        Assertions.assertThat(specItems).hasSize(4);
        // There should be no tags
        int totalTagLen = specItems.stream()
            .map(specItem -> specItem.getTagInfo().getTags())
            .collect(Collectors.joining(""))
            .length();
        Assertions.assertThat(totalTagLen).isEqualTo(0);

        // Step 2: Add some tags
        final List<String> tags = List.of("Tag1", "Tag2");
        specItems.forEach(specItem ->
            this.specItemService.saveTags(specItem, tags));
        // Note that adding the tags creates new spec items

        // Step 3: Update spec items
        // The updated spec items should have the tags now
        this.specItemService.saveDocument("file2.txt");
        // Finally, we should have 12 spec items:
        // The first four that were initially created
        // The next four are created because of the addition of tags.
        // The next four that are updated
        final List<SpecItem> oldTaggedAndUpdated = this.specItemRepo.findAll();
        Assertions.assertThat(oldTaggedAndUpdated).hasSize(12);

        // The updated spec items should have tags
        // Take last four spec items
        final List<String> tagsOfUpdatedSpecItems = oldTaggedAndUpdated
            .subList(oldTaggedAndUpdated.size() - 5, oldTaggedAndUpdated.size() - 1)
            .stream()
            .map(specItem -> specItem.getTagInfo().getTags())
            .collect(Collectors.toList());
        final long distinctTags = tagsOfUpdatedSpecItems.stream()
            .distinct()
            .count();
        Assertions.assertThat(distinctTags).isEqualTo(1);
        Assertions.assertThat(tagsOfUpdatedSpecItems.get(0)).isEqualTo("Tag1, Tag2");

    }

}
