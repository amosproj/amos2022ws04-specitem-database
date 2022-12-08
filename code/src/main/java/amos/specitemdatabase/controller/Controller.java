package amos.specitemdatabase.controller;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.service.FileStorageService;
import amos.specitemdatabase.service.SpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Controller {
    private final SpecItemService service;
    private final FileStorageService fileStorageService;

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
            Optional<SpecItem> specItem = Optional.ofNullable(service.getSpecItemById(id));
            if (specItem.isPresent()) {
                return new ResponseEntity<>(specItem.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/get/history/{id}")
    public ResponseEntity<List<SpecItem>> getSpecItemsById(@PathVariable(value = "id")String id) {
        try {
            List<SpecItem> specItemsList = service.getListOfSpecItemsById(id);
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
