package amos.specitemdatabase.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

// TODO: implement the corrent logic for this class
@Service
public class ParserService {

    public ParserService() {
    }

    public List<Map<String, String>> parse(final String fileName) {
        // TODO: access a file from its name
        //File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
        final List<Map<String, String>> specItems = new ArrayList<>();
        final Map<String, String> specItem1 =  Map.of(
            "Fingerprint", "abc",
            "ShortName", "ID1",
            "Category", "Cat1",
            "LC-Status", "VALID",
            "UseInstead", "",
            "TraceRefs", "ID2, ID3, ID4",
            "LongName", "LongName1",
            "Content", "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g");
        final Map<String, String> specItem2 =  Map.of(
            "Fingerprint", "def",
            "ShortName", "ID2",
            "Category", "Cat2",
            "LC-Status", "INVALID",
            "UseInstead", "",
            "TraceRefs", "ID23, ID3, ID43",
            "LongName", "LongName2",
            "Content", "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g");
        specItems.add(specItem1);
        specItems.add(specItem2);
        return specItems;
    }
}
