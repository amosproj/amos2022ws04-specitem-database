package amos.specitemdatabase.importer;

import amos.specitemdatabase.model.SpecItem;
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
        SpecItemParser specItemParser = new SpecItemParser();

        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
        List<SpecItem> specItems = specItemParser.fileToSpecItems(file);
        //String separator = System.lineSeparator();
        Assertions.assertEquals(2, specItems.size(), "Commit and SpecItems were not split correctly");
//        Assertions.assertEquals("CommitHash: #asdf" + separator +
//                "CommitDate: 12.02.2022" + separator +
//                "CommitMsg: asdfaf" + separator +
//                "CommitAuthor: asdfasdf", specItems.get(0), "Commit was not split correctly");
//        Assertions.assertEquals("Fingerprint: abc" + separator +
//                "ShortName: ID1" + separator +
//                "Category:  Cat1" + separator +
//                "LC-Status: VALID" + separator +
//                "UseInstead:" + separator +
//                "TraceRefs: ID2, ID3, ID4" + separator +
//                "LongName:  bla bla bla" + separator +
//                "Content:" + separator +
//                "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g" + separator +
//                "dsalhfjakdlfkdslajf;l j,, ,,,dafkdsajf j;", specItems.get(1), "First SpecItem was not split correctly");
//        Assertions.assertEquals("Fingerprint: def" + separator +
//                "ShortName: ID22" + separator +
//                "Category:  Cat2" + separator +
//                "LC-Status: INVALID" + separator +
//                "UseInstead:" + separator+
//                "TraceRefs: ID2, ID3, ID4" + separator +
//                "LongName:  bla bla bla bla bal abla" + separator +
//                "Content:  fdasfasdfds:", specItems.get(2), "Second SpecItem was not split correctly");
    }
}
