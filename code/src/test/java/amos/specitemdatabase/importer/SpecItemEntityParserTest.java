package amos.specitemdatabase.importer;

import amos.specitemdatabase.model.ProcessedDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@SpringBootTest
public class SpecItemEntityParserTest {

    @Test
    public void testParser() throws IOException {
        SpecItemParser specItemParser = new SpecItemParser();

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
        ProcessedDocument specItemsDoc = specItemParser.processFile(file);
        Assertions.assertEquals(2, specItemsDoc.getSpecItems().size(), "SpecItems were not split correctly");

    }
}
