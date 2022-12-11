package amos.specitemdatabase.controller;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.service.FileStorageService;
import amos.specitemdatabase.service.SpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemException;
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

    private void printErrorMessage(Exception e) {
        if (e != null)
            System.err.println(e.getMessage());
    }

    private <T> ResponseEntity<T> handleStatusCode400(Exception e, Class<T> type) {
        printErrorMessage(e);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private <T> ResponseEntity<T> handleStatusCode404(Exception e, Class<T> type) {
        printErrorMessage(e);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private <T> ResponseEntity<T> handleStatusCode500(Exception e, Class<T> type) {
        printErrorMessage(e);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
    private ResponseEntity<SpecItem> returnSpecItemAndStatusCode(Optional<SpecItem> specItem) {
        try {
            if (specItem.isPresent()) {
                return new ResponseEntity<>(specItem.get(), HttpStatus.OK);
            }
            return handleStatusCode404(null, SpecItem.class);
        } catch (Exception e) {
            return handleStatusCode500(e, SpecItem.class);
        }
    }
    
    private boolean isListOfSpecItemsPresentAndNotEmpty(Optional<List<SpecItem>> listOfSpecItems) {
        return listOfSpecItems.isPresent() && ! listOfSpecItems.get().isEmpty();
    }
    
    @SuppressWarnings("unchecked")
    private ResponseEntity<List<SpecItem>> returnListOfSpecItemAndStatusCode(Optional<List<SpecItem>> listOfSpecItems) {
        try {
            if (isListOfSpecItemsPresentAndNotEmpty(listOfSpecItems)) {
                return new ResponseEntity<>(listOfSpecItems.get(), HttpStatus.OK);
            }
            return handleStatusCode404(null, (Class<List<SpecItem>>) (Class<?>) List.class);
        } catch (Exception e) {
            return handleStatusCode404(null, (Class<List<SpecItem>>) (Class<?>) List.class);
        }
    }
    
    @PostMapping("upload/{filename}")
    public ResponseEntity<String> uploadDocument (@PathVariable(name="filename") String filename, @RequestParam("file") MultipartFile uploadedFile) {
        try {
            fileStorageService.storeFile(uploadedFile, filename);
            service.saveDocument(filename);

            // Kevin: Windows 11 restricted the deleting function. SpecItems will be displayed on the web pagecorrectly, but the tmp file will not be deleted.
            fileStorageService.deleteFile(filename);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (FileSystemException e) {
            return handleStatusCode500(e, String.class);
            
        } catch (Exception e) {
            return handleStatusCode400(e, String.class);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpecItem> getSpecItemById(@PathVariable(value = "id")String id) {
        Optional<SpecItem> specItem = Optional.ofNullable(service.getSpecItemById(id));
        return returnSpecItemAndStatusCode(specItem);
    }
    
    @GetMapping("/get/history/{id}")
    public ResponseEntity<List<SpecItem>> getSpecItemsById(@PathVariable(value = "id")String id) {
        Optional<List<SpecItem>> listOfSpecItems = Optional.ofNullable(service.getListOfSpecItemsById(id));
        return returnListOfSpecItemAndStatusCode(listOfSpecItems);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<SpecItem>> getAllSpecItems(@RequestParam(defaultValue = "1") int page) {
        Optional<List<SpecItem>> listOfSpecItems = Optional.ofNullable(service.getAllSpecItems(page));
        return returnListOfSpecItemAndStatusCode(listOfSpecItems);
    }

    @GetMapping("/get/cont:{content}")
    public ResponseEntity<List<SpecItem>> getSpecItemByContent(@PathVariable(value = "content")String content, @RequestParam(defaultValue = "1") int page) {
        Optional<List<SpecItem>> listOfSpecItems = Optional.ofNullable(service.getSpecItemByContent(content, page));
        return returnListOfSpecItemAndStatusCode(listOfSpecItems);
    }

    @GetMapping("/pageNumber")
    public ResponseEntity<Integer> getPageNumber() {
        int pageNumber = service.getPageNumber();
        return new ResponseEntity<>(pageNumber, HttpStatus.OK);
    }
}
