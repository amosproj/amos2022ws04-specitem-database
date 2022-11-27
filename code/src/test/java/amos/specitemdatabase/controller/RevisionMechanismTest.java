package amos.specitemdatabase.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import amos.specitemdatabase.repo.SpecItemRepo;
import java.io.InputStream;
import java.util.Objects;
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

        final InputStream firstFileInputStream = this.getClass().getClassLoader()
            .getResourceAsStream("testfiles/firstfile.txt");
        final InputStream secondFileInputStream = this.getClass().getClassLoader()
            .getResourceAsStream("testfiles/secondfile.txt");

        final MockMultipartFile firstFile = new MockMultipartFile(
            "file1",
            "firstfile.txt",
            MediaType.TEXT_PLAIN_VALUE,
            Objects.requireNonNull(firstFileInputStream).readAllBytes()
        );
        final MockMultipartFile secondFile = new MockMultipartFile(
            "file2",
            "secondfile.txt",
            MediaType.TEXT_PLAIN_VALUE,
            Objects.requireNonNull(secondFileInputStream).readAllBytes()
        );

        firstFileInputStream.close();
        secondFileInputStream.close();

        mockMvc.perform(multipart("/upload/firstfile").file(firstFile))
            .andExpect(status().isCreated());

        assertEquals(2, specItemRepo.count());


    }

}
