package amos.specitemdatabase.controller;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    //test Controller ONLY
    @Test
    void uploadDocument() throws IOException {
        Response response = send("http://localhost:8080/upload/test-file", "POST", "hi");
        System.out.println(response.body);
        assertEquals(201, response.status);
    }

    private class Response {
        public int status;
        public String body;
    }

    //send a Http request to the url, TEST ONLY
    private Response send(String urlString, String method, String body) throws IOException {
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
        Response response = new Response();
        response.status = status;
        response.body = content.toString();
        return response;
    }
}