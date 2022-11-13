package amos.specitemdatabase.filegenerator;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


public class SpecItemProviderTest {

    private ContentCreator mockedContentCreator = getMockedContentCreator();
    private SpecItemProviderImpl specItemProvider = new SpecItemProviderImpl(
        mockedContentCreator
    );

    @Test
    public void testSpecItemProvider() {
        specItemProvider.initSuppliers();
        List<Map<String, String>> specItems =
            this.specItemProvider.generateSpecItems(true, 2);
        Assertions.assertThat(specItems).hasSize(2);

    }

    private ContentCreator getMockedContentCreator() {
        return new ContentCreator() {
            @Override
            public String createContent() {
                return "content";
            }

            @Override
            public String get() {
                return this.createContent();
            }
        };
    }
}
