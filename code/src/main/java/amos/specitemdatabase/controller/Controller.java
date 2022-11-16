package amos.specitemdatabase.controller;

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

@RestController
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

    // @GetMapping("/get/{id}")
    // public ResponseEntity<String> getSpecItemById(@PathVariable(value = "id")String id) {
    //     fileStorageService.getSpecItemById()
    //     // return repo.findById(id);
    // }
}
