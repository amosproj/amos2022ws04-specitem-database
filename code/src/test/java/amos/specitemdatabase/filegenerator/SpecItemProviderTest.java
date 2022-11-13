package amos.specitemdatabase.filegenerator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
        this.specItemProvider.initSuppliers();
        final List<LinkedHashMap<String, String>> specItems =
            this.specItemProvider.generateSpecItems(true, 2);
        Assertions.assertThat(specItems).hasSize(2);
    }

    @Test
    public void testIfUpdatedSpecHasFourFieldsNonEmpty() {
        this.specItemProvider.initSuppliers();
        final List<LinkedHashMap<String, String>> specItems =
            this.specItemProvider.generateSpecItems(false, 2);
        Assertions.assertThat(specItems).hasSize(2);
        int emptyFields = (int) specItems.get(0).values().stream().filter(String::isEmpty).count();
        Assertions.assertThat(emptyFields).isEqualTo(4);
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
