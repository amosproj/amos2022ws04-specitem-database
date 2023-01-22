package amos.specitemdatabase.controller;

import amos.specitemdatabase.repo.SpecItemRepo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ControllerRaceConditionTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    SpecItemRepo specItemRepo;

    private String fileContent;

    private int numberOfThreads = 3;
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        fileContent = "CommitHash: #asdf\n" +
                "CommitDate: 2022-02-12 21:49:13\n" +
                "CommitMsg: bla bla prank\n" +
                "CommitAuthor: Mister Wallace\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Fingerprint: abc\n" +
                "ShortName: ID1\n" +
                "Category: Category1\n" +
                "LC-Status: Status1\n" +
                "UseInstead: \n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName: bla bla bla\n" +
                "Content: \n" +
                "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g\n" +
                "dsalhfjakdlfkdslajf;l j,, ,,,dafkdsajf j;\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n";
    }

    @Test
    public void testSaveDocument() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );
        UploadDocumentThread[] threads = new UploadDocumentThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            //send file
            threads[i] = new UploadDocumentThread(file);
            threads[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }

        assertEquals(1, specItemRepo.count());
    }

    private class UploadDocumentThread extends Thread{
        private MockMultipartFile file;
        public UploadDocumentThread(MockMultipartFile file) {
            this.file = file;
        }

        @SneakyThrows
        @Override
        public void run() {
            mockMvc.perform(multipart("/upload/test-file").file(file));
        }
    }
}
