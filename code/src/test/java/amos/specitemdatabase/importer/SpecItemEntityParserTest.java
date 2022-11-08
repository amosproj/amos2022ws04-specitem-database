package amos.specitemdatabase.importer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class SpecItemEntityParserTest {

    @Test
    public void testParser() throws IOException {

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
        List<String> specItems = SpecItemParser.splitFileIntoSpecItems(file);
        Assertions.assertEquals(3, specItems.size(), "Commit and SpecItems were not split correctly");
        Assertions.assertEquals("CommitHash: #asdf\n" +
                "CommitDate: 12.02.2022\n" +
                "CommitMsg: asdfaf\n" +
                "CommitAuthor: asdfasdf", specItems.get(0), "Commit was not split correctly");
        Assertions.assertEquals("Fingerprint: def\n" +
                "ShortName: ID22\n" +
                "Category:  Cat2\n" +
                "LC-Status: INVALID\n" +
                "UseInstead:\n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName:  bla bla bla bla bal abla\n" +
                "Content:  fdasfasdfds:", specItems.get(2), "Second SpecItem was not split correctly");
    }
}
