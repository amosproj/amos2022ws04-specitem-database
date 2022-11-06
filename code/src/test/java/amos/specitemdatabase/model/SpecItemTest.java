package amos.specitemdatabase.model;

import amos.specitemdatabase.importer.ParserService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpecItemTest {

    @Autowired
    private ParserService parserService;

    @Test
    void shouldInstantiateSpecItemFromMap() {

        final List<Map<String, String>> specItems = parserService.parse("dummy");
        SpecItemBuilder specItemBuilder = new SpecItemBuilder();
        specItemBuilder.fromMap(specItems.get(0));

        SpecItem specItem = new SpecItem(specItemBuilder);

        // todo: import junit


    }

}
