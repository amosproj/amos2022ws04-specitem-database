package amos.specitemdatabase.model;

import amos.specitemdatabase.importer.ParserService;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


public class SpecItemTest {

    private final ParserService parserService = new ParserService();

    @Test
    void shouldInstantiateSpecItemWithMandatoryFields() {

        final List<Map<String, String>> specItems = this.parserService.parse("dummy");
        final SpecItemBuilder specItemBuilder = new SpecItemBuilder();
        specItemBuilder.fromMap(specItems.get(0));

        final SpecItem specItem = new SpecItem(specItemBuilder);

        Assertions.assertThat(specItem.getShortName()).isEqualTo("ID1");
        Assertions.assertThat(specItem.getLcStatus()).isEqualTo(LcStatus.STATUS1);
        Assertions.assertThat(specItem.getCategory()).isEqualTo(Category.CONSTRAINT_ITEM);
        Assertions.assertThat(specItem.getLongName()).isEqualTo("LongName1");
        Assertions.assertThat(specItem.getContent()).isEqualTo("Content1");
    }

}
