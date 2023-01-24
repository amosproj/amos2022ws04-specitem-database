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

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private int numberOfThreads = 10;
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

    //TODO: test case for delete specitem and addtag

    @Test
    public void testSaveDocument() throws Exception {
        UploadDocumentThread[] threads = new UploadDocumentThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            //send file
            threads[i] = new UploadDocumentThread(i);
            threads[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }
        assertEquals(1, specItemRepo.count());
        System.out.println(specItemRepo.getLatestSpecItemByID("ID1").getContent());
    }

    @Test
    public void testAddTags() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );
        mockMvc.perform(multipart("/upload/test-file").file(file));
        //INCOMPLETE BECAUSE THE ADD TAG FUNCTION STILL NEED TO BE CHANGED
        AddTagThread[] threads = new AddTagThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            //send file
            threads[i] = new AddTagThread(i);
            threads[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }
        String expectedTag = "Key 1:Value1,Key 2:Value2,Key 3:Value3,Key 4:Value4,Key 5:Value5,Key 6:Value6,Key 7:Value7,Key 8:Value8,Key 9:Value9,Key 10:Value10";
        assertEquals(expectedTag, specItemRepo.getLatestSpecItemByID("ID1").getTagInfo().getTags());
    }

    private class AddTagThread extends Thread {
        private int i;
        String tag;
        public AddTagThread(int i) {
            this.i = i;
            this.tag = "Key " + i + ":Value " +i;
        }

        @Override
        public void run() {
            //TODO: send add tags request
        }
    }

    private class UploadDocumentThread extends Thread{
        private int i;
        private String fileString;
        public UploadDocumentThread(int i) {
            this.i = i;
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            this.fileString = "CommitHash: #asdf\n" +
                    "CommitDate: "+ timeStamp + "\n" +
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
                    "Thread " + i +"\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n";
        }

        @SneakyThrows
        @Override
        public void run() {
            MockMultipartFile file
                    = new MockMultipartFile(
                    "file",
                    "test-file.txt",
                    MediaType.TEXT_PLAIN_VALUE,
                    fileString.getBytes()
            );
            mockMvc.perform(multipart("/upload/file"+i).file(file));
        }
    }
}
