package amos.specitemdatabase.controller;

import amos.specitemdatabase.model.Category;
import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemBuilder;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.service.FileStorageService;
import amos.specitemdatabase.service.SpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.json.JSONObject;;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {

    private final FileStorageService fileStorageService;
    private final SpecItemService service;
    @Autowired
    private SpecItemRepo specItemRepo;
    @Autowired
    public Controller(SpecItemService service, FileStorageService fileStorageService) {
        this.service = service;
        this.fileStorageService = fileStorageService;
    }

    /***
     * upload a new document to the database, response status code 201 if successful
     * @param filename name of the document
     * @param file the text file
     * @return
     */
    @PostMapping("upload/{filename}")
    public ResponseEntity<String> uploadDocument (@PathVariable(name="filename") String filename,
                                                  @RequestParam("file") MultipartFile file) {
        //saving content to a file in /tmp foler
        System.out.println("Get a POST Request");
        try {
            fileStorageService.storeFile(file, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //saving document to database
        try {
            service.saveDocument(filename);
            fileStorageService.deleteFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("Upload Successful!");
        return new ResponseEntity<>("Upload Successful!", HttpStatus.CREATED);
    }

    /***
     * update the tags of specitem, response status code 201 if successful
     * @param tags name of the document
     *
     * @return
     */
    @PostMapping(path = "post/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTags (@RequestBody String tags){
        //saving content to a file in /tmp foler
        System.out.println("Get a POST Request");


        //saving document to database
        try {
            //create Json object from Json string
            JSONObject json = new JSONObject(tags);
            //System.out.println(tags);

            // create Specitem Builder and fill it with attributes
            SpecItemBuilder sb = new SpecItemBuilder();
            sb.fromStringRepresentation(json.getString("shortname"),json.getString("category"),json.getString("lcStatus"),json.getString("longname"),json.getString("content"));

            //parse tracerefs
            sb.setTraceRefs(json.getString("traceref").substring(1,json.getString("traceref").length()-1));

            //parse Local date time
            String[] dateParts = json.getString("commitTime").replace("[", "").replace("]", "").split(",");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            int hour = Integer.parseInt(dateParts[3]);
            int minute = Integer.parseInt(dateParts[4]);
            int second = Integer.parseInt(dateParts[5]);
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute, second);

            //Create the commit from Json object and setCommit for specitem builder
            Commit c = new Commit(json.getString("commitHash"),json.getString("commitMsg"),dateTime,json.getString("commitAuthor"));
            sb.setCommit(c);

            //create specitem
            SpecItem s = new SpecItem(sb);
            System.out.println("Helloo " +service.getSpecItemById(json.getString("shortname")));
            //SpecItem s2 = service.getSpecItemById(json.getString("shortname"));


            String taglist = json.getString("tagList");
            // Split the input string on spaces


            // Create a List from the resulting array
            List<String> stringArrayList = Arrays.asList(taglist);
            System.out.println("SpecItem =" + s.getShortName());
            service.saveTags(s, stringArrayList);
            //String stringValue = json.getString("tagList");

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("Upload Successful!");
        return new ResponseEntity<>("Upload Successful!", HttpStatus.CREATED);
    }

    @GetMapping("/get/cont:{content}")
    public ResponseEntity<List<SpecItem>> getSpecItemByContent(@PathVariable(value = "content")String content,
                                                               @RequestParam(defaultValue = "1") int page) {
        try {
            List<SpecItem> specItemsList = service.getSpecItemByContent(content, page);
            System.out.println("Getting SpecItems by content...");
            return new ResponseEntity<>(specItemsList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpecItem> getSpecItemById(@PathVariable(value = "id")String id) {
        try {
            SpecItem specItem = service.getSpecItemById(id);
            System.out.println("Getting SpecItem by ID...");
            return new ResponseEntity<>(specItem, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/history/{id}")
    public ResponseEntity<List<SpecItem>> getSpecItemsById(@PathVariable(value = "id")String id) {
        try {
            List<SpecItem> specItemsList = service.getSpecItemsById(id);
            System.out.println("Getting SpecItem history by ID...");
            return new ResponseEntity<>(specItemsList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<SpecItem>> getAllSpecItems(@RequestParam(defaultValue = "1") int page) {
        try {
            List<SpecItem> specItem = service.getAllSpecItems(page);
            System.out.println("Processing...");
            return new ResponseEntity<>(specItem, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/pageNumber")
    public ResponseEntity<Integer> getPageNumber() {
        int pageNumber = service.getPageNumber();
        return new ResponseEntity<>(pageNumber, HttpStatus.OK);
    }
}
