package amos.specitemdatabase.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    //test Controller ONLY
    @Test
    void uploadDocument() throws IOException {
        String body= "CommitHash: #asdf\n" +
                "CommitDate: 12.02.2022\n" +
                "CommitMsg: asdfaf\n" +
                "CommitAuthor: asdfasdf\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Fingerprint: abc\n" +
                "ShortName: ID1\n" +
                "Category:  CONSTRAINT_ITEM\n" +
                "LC-Status: Status1\n" +
                "UseInstead:\n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName:  bla bla bla\n" +
                "Content:\n" +
                "fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g\n" +
                "dsalhfjakdlfkdslajf;l j,, ,,,dafkdsajf j;\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Fingerprint: def\n" +
                "ShortName: ID22\n" +
                "Category:  UNCONSTRAINED_ITEM\n" +
                "LC-Status: Status2\n" +
                "UseInstead:\n" +
                "TraceRefs: ID2, ID3, ID4\n" +
                "LongName:  bla bla bla bla bal abla\n" +
                "Content:  \n" +
                "fdasfasdfds:";
        Response response = send("http://localhost:8080/upload/test-file", "POST", body);
        System.out.println(response.body);
        assertEquals(201, response.status, response.body);
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