package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItem;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaggingMechanismTest {

    @Autowired
    private SpecItemService specItemService;

    @Test
    void shouldAddTagsToUpdatedSpecItems() throws IOException {

        // Step 1: newly created spec items have no tags
        specItemService.saveDocument("file1.txt");
        // There should be 4 spec items in the DB
        final List<SpecItem> specItems = specItemService.getAllSpecItems();
        Assertions.assertThat(specItems).hasSize(4);
        // There should be no tags
        int totalTagLen = specItems.stream()
            .map(specItem -> specItem.getTagInfo().getTags())
            .collect(Collectors.joining(""))
            .length();
        Assertions.assertThat(totalTagLen).isEqualTo(0);

        // Step 2:



    }

}
