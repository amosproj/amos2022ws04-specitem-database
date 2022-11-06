package amos.specitemdatabase.importer;

import amos.specitemdatabase.importer.SpecItemParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

@SpringBootTest
public class SpecItemEntityParserTest {

    @Test
    public void testParser() throws IOException {

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
        SpecItemParser.splitFileIntoSpecItems(file);

    }
}
