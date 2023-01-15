package amos.specitemdatabase.controller;

import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.CompareResult;
import amos.specitemdatabase.model.CompareResultMarkup;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemBuilder;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.service.FileStorageService;
import amos.specitemdatabase.service.SpecItemService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import java.nio.file.FileSystemException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@CrossOrigin("*")
@Slf4j
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
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception e) {
            return handleStatusCode500(e, SpecItem.class);
        }
    }

    @SuppressWarnings("unchecked")
    private ResponseEntity<List<SpecItem>> returnListOfSpecItemAndStatusCode(Optional<List<SpecItem>> listOfSpecItems) {
        try {
            if (listOfSpecItems.isPresent()) {
                List<SpecItem> list = listOfSpecItems.get();
                return new ResponseEntity<>(listOfSpecItems.get(), HttpStatus.OK);
            }
            return handleStatusCode404(null, (Class<List<SpecItem>>) (Class<?>) List.class);
        } catch (Exception e) {
            return handleStatusCode404(null, (Class<List<SpecItem>>) (Class<?>) List.class);
        }
    }

    private String getDecodedURLWithoutSpecialCharacters(String urlWithSpecialCharacters) {
        String decodedURL = "";
        try {
            urlWithSpecialCharacters = urlWithSpecialCharacters.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            urlWithSpecialCharacters = urlWithSpecialCharacters.replaceAll("\\+", "%2B");

            decodedURL = URLDecoder.decode(urlWithSpecialCharacters, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getMessage());
			return decodedURL;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return decodedURL;
    }

    /***
     * update the tags of a specitem, response status code 201 if successful
     * @param specItemAsJsonString name of the document
     */
    @PostMapping(path = "post/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateTags(@RequestBody final String specItemAsJsonString){
        //saving content to a file in /tmp folder
        log.info("Received a request for updating the tags.");
        try {
            // Create a json object from the json string representation of a spec item
            JSONObject json = new JSONObject(specItemAsJsonString);

            // Save tags to the tags table, which involves:
            // 1. fetching current tags for ID & Time
            // 2. saving the previous + new tags



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
            System.out.println("Helloo " + service.getSpecItemById(json.getString("shortname")));
            //SpecItem s2 = service.getSpecItemById(json.getString("shortname"));

            String tagList = json.getString("tagList");
            // Split the input string on spaces

            // Create a List from the resulting array
            List<String> stringArrayList = Collections.singletonList(tagList);
            System.out.println("SpecItem =" + s.getShortName());
            service.fetchAndSave(s, stringArrayList, false);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        System.out.println("Upload Successful!");
        return new ResponseEntity<>("Upload Successful!", HttpStatus.CREATED);
    }

    
    @PostMapping("upload/{filename}")
    public ResponseEntity<String> uploadDocument (@PathVariable(name="filename") String filename, @RequestParam("file") MultipartFile uploadedFile) {

        try {
            fileStorageService.storeFile(uploadedFile, filename);
            service.saveDocument(filename);

            // Kevin: Windows 11 restricted the deleting function. SpecItems will be displayed on the web page correctly, but the tmp file will not be deleted.
            fileStorageService.deleteFile(filename);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (FileSystemException e) {
            return handleStatusCode500(e, String.class);
            
        } catch (Exception e) {
            return handleStatusCode400(e, String.class);
        }
    }

    // @DeleteMapping("/delete/{specItemId}&{documentId}")
    // public ResponseEntity<String> deleteSpecItemById(@PathVariable(value =
    // "specItemId")String specItemId, @PathVariable(value = "documentId")String
    // documentId) {
    @DeleteMapping("/delete/{specItemId}")
    public ResponseEntity<String> deleteSpecItemById(@PathVariable(value = "specItemId")String specItemId) {
        try {
            // service.deleteSpecItemById(specItemId, documentId);
            service.deleteSpecItemById(specItemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return handleStatusCode400(e, String.class);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<SpecItem> getSpecItemById(@PathVariable(value = "id")String id) {
        id = getDecodedURLWithoutSpecialCharacters(id);
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
    public ResponseEntity<List<SpecItem>> getSpecItemByContent(@PathVariable(value = "content") String content, @RequestParam(defaultValue = "1") int page) throws UnsupportedEncodingException {
		content = getDecodedURLWithoutSpecialCharacters(content);
        Optional<List<SpecItem>> listOfSpecItems = Optional.ofNullable(service.getListOfSpecItemsByContent(content, page));
        return returnListOfSpecItemAndStatusCode(listOfSpecItems);
    }

    @GetMapping("/get/cont:{content}/id:{id}")
    public ResponseEntity<List<SpecItem>> filterSpecitemHistoryByContent(@PathVariable("content") String content,
                                                                         @PathVariable("id") String id) {
        System.out.println(content+" "+ id);
        Optional<List<SpecItem>> listOfSpecItems = Optional.ofNullable(service.getListOfSpecItemsByIDAndContent(id, content));
        System.out.println(listOfSpecItems);
        return returnListOfSpecItemAndStatusCode(listOfSpecItems);
    }

    @GetMapping("/pageNumber")
    public ResponseEntity<Integer> getPageNumber() {
        int pageNumber = service.getPageNumber();
        return new ResponseEntity<>(pageNumber, HttpStatus.OK);
    }

    @GetMapping("/get/pageNumber/{specitem}")
    public ResponseEntity<Integer> getPageNumberOfSpecItem(@PathVariable(value = "specitem")String specitem) {
    	for (int i = 1; i <= service.getPageNumber(); i++) {
    		List<SpecItem> listOfSpecItems = service.getAllSpecItems(i);
    		for(SpecItem s : listOfSpecItems) {
    			if (s.getShortName().equals(specitem)) {
    				return new ResponseEntity<>(i, HttpStatus.OK);
    			}
    		}
    	}
    	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/compare/{shortName}")
    public ResponseEntity compareVersions(@PathVariable(value = "shortName") String shortName,
                                          @RequestParam("old") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime old,
                                          @RequestParam("new") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updated) {
        try {
            System.out.println("GET compare versions of "+ shortName + " between " + old + " and " + updated);
            List<CompareResult> results = service.compare(shortName, old, updated);
            return ResponseEntity.status(HttpStatus.OK).body(results);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/compare/markup/{shortName}")
    public ResponseEntity compareVersionsMarkup(@PathVariable(value = "shortName") String shortName,
                                          @RequestParam("old") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime old,
                                          @RequestParam("new") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updated) {
        try {
            System.out.println("GET compare versions of "+ shortName + " between " + old + " and " + updated);
            List<CompareResultMarkup> results = service.compareMarkup(shortName, old, updated);
            return ResponseEntity.status(HttpStatus.OK).body(results);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
