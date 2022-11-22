package amos.specitemdatabase.filegenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;


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
