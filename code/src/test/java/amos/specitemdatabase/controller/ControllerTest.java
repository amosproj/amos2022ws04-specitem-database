package amos.specitemdatabase.controller;

import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.service.SpecItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class ControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    SpecItemRepo specItemRepo;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    //test Controller ONLY
    //Tested by using Postman
    @Test
    void testUploadDocument() throws Exception {
        String fileContent = "CommitHash: #asdf\n" +
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
                "\n" +
                "Fingerprint: def\n" +
                "ShortName: ID22\n" +
                "Category: Category2\n" +
                "LC-Status: Status2\n" +
                "UseInstead: \n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName: bla bla bla bla bal abla\n" +
                "Content: \n" +
                "fdasfasdfds:\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n";
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );
        //send file
        mockMvc.perform(multipart("/upload/test-file").file(file))
                .andExpect(status().isCreated());
        assertEquals(2, specItemRepo.count());
    }
    @Test
    void testGetDocument() throws Exception {
        String fileContent = "CommitHash: #asdf\n" +
                "CommitDate: 2022-02-12 21:49:13\n" +
                "CommitMsg: bla bla prank\n" +
                "CommitAuthor: Mister Wallace\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Fingerprint: abd\n" +
                "ShortName: ID1\n" +
                "Category: Category1\n" +
                "LC-Status: Status1\n" +
                "UseInstead: \n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName: l1\n" +
                "Content: \n" +
                "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g\n" +
                "dsalhfjakdlfkdslajf;l j,, ,,,dafkdsajf j;\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n";
        String fileContent2 = "CommitHash: #asdf\n" +
                "CommitDate: 2022-02-12 20:49:13\n" +
                "CommitMsg: bla bla prank\n" +
                "CommitAuthor: Mister Wallace\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Fingerprint: abc\n" +
                "ShortName: ID1\n" +
                "Category: Category2\n" +
                "LC-Status: Status1\n" +
                "UseInstead: \n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName: l2\n" +
                "Content: \n" +
                "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g\n" +
                "dsalhfjakdlfkdslajf;l j,, ,,,dafkdsajf j;\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n";
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "file",
                "test-file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent2.getBytes()
        );
        //send file
        mockMvc.perform(multipart("/upload/test-file").file(file))
                .andExpect(status().isCreated());
        mockMvc.perform(multipart("/upload/test-file").file(file2))
                .andExpect(status().isCreated());
        mockMvc.perform( MockMvcRequestBuilders
                .get("/get/{id}", "ID1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.longName").value("l1"))
                .andExpect(status().isOk());
        assertEquals(2, specItemRepo.count());
    }


    private class Response {
        public int status;
        public String body;
    }

    //send a Http request to the url, TEST ONLY
    private Response send(String urlString, String method, String body) throws IOException {
        Response response = new Response();
        try {
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(2000);
            con.setReadTimeout(2000);
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(body);
            osw.close();
            os.close();
            con.connect();
            int status = con.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while((inputLine =  reader.readLine()) != null) {
                content.append(inputLine);
            }
            reader.close();
            con.disconnect();
            response.status = status;
            response.body = content.toString();
        } catch (IOException e) {
            response.status = 400;
            response.body = e.getMessage();
        }
        return response;
    }
}