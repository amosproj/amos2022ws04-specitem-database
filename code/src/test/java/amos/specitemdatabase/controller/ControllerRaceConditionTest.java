package amos.specitemdatabase.controller;

import amos.specitemdatabase.model.Category;
import amos.specitemdatabase.model.LcStatus;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.SpecItemRepo;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                "content\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n";
    }

    //TODO: test case for delete specitem and addtag
    @Test void testDelete() throws Exception {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );
        mockMvc.perform(multipart("/upload/test-file").file(file));
        DeleteThread deleteThread = new DeleteThread();
        deleteThread.start();
        AddTagThread[] threads = new AddTagThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            //send file
            threads[i] = new AddTagThread(i);
            threads[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }
        deleteThread.join();
        assertTrue(specItemRepo.getLatestSpecItemByID("ID1").isMarkedAsDeleted());
    }

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

        AddTagThread[] threads = new AddTagThread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            //send file
            threads[i] = new AddTagThread(i);
            threads[i].start();
        }
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }
        List<SpecItem> specItems = specItemRepo.findAll();
        assertEquals(1, specItemRepo.getCount());
        String actualTag = specItemRepo.getLatestSpecItemByID("ID1").getTagInfo().getTags();
        System.out.println(actualTag);
        for (int i = 0; i < numberOfThreads; i++) {
            assert actualTag.contains("Key " + i + ":Value " +i);
        }
    }

    private class AddTagThread extends Thread {
        private int i;
        String json;
        public AddTagThread(int i) {
            this.i = i;
            String tag = "Key " + i + ":Value " +i;
            LocalDateTime now = LocalDateTime.now();
            String time = "[" + now.getYear() + "," + now.getMonth().getValue() + "," + now.getDayOfMonth() + "," + now.getHour() + "," + now.getMinute()+ "," + now.getSecond() + "]";
            this.json = "{\"fingerprint\":\"abc\",\"shortname\":\"ID1\",\"commitTime\":" + time + ",\"markedAsDeleted\":false,\"category\":\"CATEGORY1\",\"lcStatus\":\"STATUS1\",\"traceref\":[\"ID2\",\"ID3\",\"ID4\"],\"longname\":\"bla bla bla\",\"content\":\"content\",\"tagInfo\":{\"version\":0,\"tags\":\""+ tag +"\"},\"tagList\":[\"" + tag +"\"]}";
            System.out.println(json);
        }

        @SneakyThrows
        @Override
        public void run() {
            mockMvc.perform(post("/post/tags")
                    .contentType(new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8))
                    .content(json));
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
    private class DeleteThread extends Thread{
        @SneakyThrows
        @Override
        public void run() {
            mockMvc.perform(delete("/delete/ID1"));
        }
    }
}
