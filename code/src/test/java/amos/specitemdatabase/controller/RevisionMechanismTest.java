package amos.specitemdatabase.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.repo.SpecItemRepo;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RevisionMechanismTest {

    // 1. add a file with SpecItem_0 and SpecItem_1
    // 2. check that there is a single item SpecItem_0
    // 3. add another file with SpecItem_0 and SpecItem_2
    // 4. check that there are two items with the
    // short name = SpecItem_0

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SpecItemRepo specItemRepo;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldRetrieveTwoVersionsOfSpecItemAfterTwoInsertions() throws Exception {

        final String firstFileContent = this.firstFileContent();

        final MockMultipartFile firstFile
            = new MockMultipartFile(
            "file",
            "test-file.txt",
            MediaType.TEXT_PLAIN_VALUE,
            firstFileContent.getBytes()
        );

        mockMvc.perform(multipart("/upload/firstfile").file(firstFile))
            .andExpect(status().isCreated());

        assertEquals(2, specItemRepo.count());

        final String secondFileContent = this.secondFileContent();

        final MockMultipartFile secondFile
            = new MockMultipartFile(
            "file",
            "test-file.txt",
            MediaType.TEXT_PLAIN_VALUE,
            secondFileContent.getBytes()
        );

        mockMvc.perform(multipart("/upload/firstfile").file(secondFile))
            .andExpect(status().isCreated());

        assertEquals(4, specItemRepo.count());

        final List<SpecItem> specItems = specItemRepo.findAll();

        Map<String, Long> specItemsToVersions = specItems.stream()
            .map(SpecItem::getShortName)
            .collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()
            ));
        assertEquals(2, specItemsToVersions.get("SpecItem_0"));

    }


    private String firstFileContent() {
        return  "CommitHash: #abc\n" +
            "CommitDate: 2022-02-12 21:00:00\n" +
            "CommitMsg: bla bla prank\n" +
            "CommitAuthor: Mister Wallace\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "Fingerprint: abc\n" +
            "ShortName: SpecItem_0\n" +
            "Category: Category1\n" +
            "LC-Status: Status1\n" +
            "UseInstead: \n" +
            "TraceRefs: SpecItem_4\n" +
            "LongName: LN1\n" +
            "Content: \n" +
            "content01\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "Fingerprint: def\n" +
            "ShortName: SpecItem_1\n" +
            "Category: Category2\n" +
            "LC-Status: Status2\n" +
            "UseInstead: \n" +
            "TraceRefs: SpecItem_2\n" +
            "LongName: LN2\n" +
            "Content: \n" +
            "content02:\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";
    }

    private String secondFileContent() {
        return  "CommitHash: #abcd\n" +
            "CommitDate: 2022-02-12 21:10:00\n" +
            "CommitMsg: bla bla prank\n" +
            "CommitAuthor: Mister Wallace\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "Fingerprint: abc\n" +
            "ShortName: SpecItem_0\n" +
            "Category: Category1\n" +
            "LC-Status: Status1\n" +
            "UseInstead: \n" +
            "TraceRefs: SpecItem_4\n" +
            "LongName: LN1\n" +
            "Content: \n" +
            "content02\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "Fingerprint: defg\n" +
            "ShortName: SpecItem_2\n" +
            "Category: Category2\n" +
            "LC-Status: Status2\n" +
            "UseInstead: \n" +
            "TraceRefs: SpecItem_2\n" +
            "LongName: LN2\n" +
            "Content: \n" +
            "content12:\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";
    }
}
