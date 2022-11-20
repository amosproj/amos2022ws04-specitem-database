package amos.specitemdatabase.controller;

import amos.specitemdatabase.repo.SpecItemRepo;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class ControllerTest {

    @Autowired
    SpecItemRepo specItemRepo;
    //test Controller ONLY
    //Tested by using Postman
    @Test
    void uploadDocument() throws IOException {
        //send file
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/upload/test-file");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody(
                "file",
                new FileInputStream(file),
                ContentType.APPLICATION_OCTET_STREAM,
                file.getName()
        );
        HttpEntity multipart = builder.build();
        post.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity responseEntity = response.getEntity();
        //read response
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while((inputLine =  reader.readLine()) != null) {
            content.append(inputLine);
        }
        reader.close();
        String responseString = content.toString();

        assertEquals("Upload Successful!", responseString, responseString);
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