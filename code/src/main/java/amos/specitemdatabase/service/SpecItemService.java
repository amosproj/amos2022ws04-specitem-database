package amos.specitemdatabase.service;

import amos.specitemdatabase.config.FileConfig;
import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.importer.SpecItemParserInterface;
import amos.specitemdatabase.model.DocumentEntity;
import amos.specitemdatabase.model.ProcessedDocument;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.repo.DocumentRepo;
import amos.specitemdatabase.repo.SpecItemRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class SpecItemService {
    private final SpecItemRepo specItemRepo;
    private final DocumentRepo documentRepo;
    private final SpecItemParserInterface specItemParser = new SpecItemParser();
    private final FileConfig fileConfig;

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo, DocumentRepo documentRepo, FileConfig fileConfig) {
        this.specItemRepo = specItemRepo;
        this.documentRepo = documentRepo;
        this.fileConfig = fileConfig;
    }

    // public ResponseEntity<SpecItemEntity> saveSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
    //     specItemRepo.save(specItemEntity);
    //     return ResponseEntity.ok(specItemEntity);
    // }
    
    // public ResponseEntity<SpecItemEntity> deleteSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
    //     specItemRepo.delete(specItemEntity);
    //     return ResponseEntity.ok(specItemEntity);
    // }
    
    // public ResponseEntity<SpecItemEntity> getSpecItemEntity(@RequestBody long id) {
    //     for(SpecItemEntity e : specItemRepo.findAll()) {
    //         if(e.getId().equals(id)) {
    //             return ResponseEntity.ok(e);
    //         }
    //     }
    //     return null;
    // }

    public List<SpecItem> getSpecItemByContent(String content) {
        List<SpecItem> specItemsList = new ArrayList<>();
        List<DocumentEntity> documentEntityList = documentRepo.findAll();
        for(DocumentEntity documentEntity: documentEntityList) {
            List<SpecItem> specItemsInDocList = documentEntity.getSpecItems();
            for (SpecItem specItem: specItemsInDocList) {
                if (specItem.getContent().contains(content)) {
                    specItemsList.add(specItem);
                }
            }
        }
        return specItemsList;
    }
    /***
     * save text file as document object and its relating Specitem objects in database
     * @param filename name of the document text file stored in tmp folder
     */
    public void saveDocument(String filename) throws IOException{
        String filepath = fileConfig.getUploadDir() + filename;
        File file = new File(filepath);
        ProcessedDocument pDoc = specItemParser.processFile(file);
//        List<SpecItemEntity> specItemEntities = pDoc.getSpecItems().stream().map(specItemParser::transformSpecItem).collect(Collectors.toList());
        DocumentEntity documentEntity = new DocumentEntity(filename, pDoc.getSpecItems(), pDoc.getCommit());
        documentRepo.save(documentEntity);
        System.out.println(specItemRepo.findAll().size());
    }

    public SpecItem getSpecItemById(String specItemId) {
        // for(DocumentEntity documentEntity:documentRepo.findAll()) {
        //     List<SpecItem> specItems=documentEntity.getSpecItems();
        //     for(SpecItem specItem:specItems) {
        //         if(specItem.getShortName()==specItemId) {
        //             // return specItem;
        //             System.out.println(specItem.toString());
        //         }
        //     }
        // }
        // return null;

        List<DocumentEntity> listDocumentEntity = documentRepo.findAll();
        for(DocumentEntity d:listDocumentEntity) {
            System.out.println(d.getCommit().toString());
            List<SpecItem> list=d.getSpecItems();
            for (SpecItem s:list) {
                if (s.getShortName().equals(specItemId)) {
                    System.out.println(s.getShortName());
                    return s;
                }
            }
        }
        return null;
    }
    
    public List<SpecItem> getSpecItemsById(String specItemId){
    	
    	List<SpecItem> allSpecItems = specItemRepo.findAll();
    	List<SpecItem> listSpecItems = new ArrayList<>();
        for(SpecItem s: allSpecItems) {
            if(s.getShortName().equals(specItemId)) {
            	listSpecItems.add(s);
            }
        }
        System.out.println(listSpecItems.size());
        if(listSpecItems.size() > 0) {
        	return listSpecItems;
        }
    	return null;
    }
      
    public SpecItem deleteSpecItemById(String specItemId) {

        List<DocumentEntity> listDocumentEntity = documentRepo.findAll();
        for(DocumentEntity d:listDocumentEntity) {

            SpecItem tmp = new SpecItem();
            boolean found = false;
            for (SpecItem s: d.getSpecItems()) {
                if (s.getShortName().equals(specItemId)) {
                    tmp = s;
                    found = true;
                }
            }
            if (found) {
            	d.getSpecItems().remove(tmp);
                documentRepo.save(d);
            }
        }
        return null;
    }

    public List<SpecItem> getAllSpecItems() {

        List<DocumentEntity> listDocumentEntity = documentRepo.findAll();
        for(DocumentEntity d:listDocumentEntity) {
            System.out.println(d.getCommit().toString());
            List<SpecItem> list=d.getSpecItems();
            if(list != null){
                return list;
            }
        }
        return null;
    }

//     @Bean
//     CommandLineRunner commandLineRunner(
//         DocumentRepo documentRepo
//     ) {
//         return args -> {
//             Commit commit = new Commit(
//                 "hash",
//                 "message",
//                 LocalDateTime.now(),
//                 "author"
//             );
//             
//             Commit commit2 = new Commit(
//                     "hash",
//                     "message",
//                     LocalDateTime.of(2019, 03, 28, 14, 33, 48, 640000),
//                     "author"
//                 );
//             
//             
//             SpecItem specItem = new SpecItem();
//             specItem.setShortName("id");
//             specItem.setContent("content");
//             specItem.setCommit(commit);
//             specItem.setFingerprint("fingerprint");
//             specItem.setLongName("longName");
//             specItem.setUseInstead("useInstead");
//             specItem.setTraceRefs(new LinkedList<>());
//             specItem.setTime(commit.getCommitTime());
//             specItem.setCategory(Category.CATEGORY1);
//             specItem.setLcStatus(LcStatus.STATUS1);
//
//             SpecItem specItem2 = new SpecItem();
//             specItem2.setShortName("id2");
//             specItem2.setContent("content");
//             specItem2.setCommit(commit);
//             specItem2.setFingerprint("fingerprint");
//             specItem2.setLongName("longName");
//             specItem2.setUseInstead("useInstead");
//             specItem2.setTraceRefs(new LinkedList<>());
//             specItem2.setTime(commit.getCommitTime());
//             specItem2.setCategory(Category.CATEGORY1);
//             specItem2.setLcStatus(LcStatus.STATUS1);
//
//             SpecItem specItem3 = new SpecItem();
//             specItem3.setShortName("id3");
//             specItem3.setContent("content");
//             specItem3.setCommit(commit);
//             specItem3.setFingerprint("fingerprint");
//             specItem3.setLongName("longName");
//             specItem3.setUseInstead("useInstead");
//             specItem3.setTraceRefs(new LinkedList<>());
//             specItem3.setTime(commit.getCommitTime());
//             specItem3.setCategory(Category.CATEGORY1);
//             specItem3.setLcStatus(LcStatus.STATUS1);
//             
//             
//             SpecItem specItem4 = new SpecItem();
//             specItem4.setShortName("id3");
//             specItem4.setContent("content4");
//             specItem4.setCommit(commit2);
//             specItem4.setFingerprint("fingerprint");
//             specItem4.setLongName("longName");
//             specItem4.setUseInstead("useInstead");
//             specItem4.setTraceRefs(new LinkedList<>());
//             specItem4.setTime(commit2.getCommitTime());
//             specItem4.setCategory(Category.CATEGORY1);
//             specItem4.setLcStatus(LcStatus.STATUS1);
//
//             List<SpecItem> specItems = new ArrayList<>();
//             specItems.add(specItem);
//             specItems.add(specItem2);
//             specItems.add(specItem3);
//             DocumentEntity documentEntity = new DocumentEntity("name",specItems,commit);
//             documentRepo.save(documentEntity);
//             
//             List<SpecItem> specItems2 = new ArrayList<>();
//             specItems2.add(specItem4);
//             DocumentEntity documentEntity2 = new DocumentEntity("name23",specItems2,commit2);
//             documentRepo.save(documentEntity2);
//             
//             getSpecItemsById("id3");
//             deleteSpecItemById(specItem2.getShortName());
//             System.out.println("Document saved.");
//
//         };
//     }
// }

//         List<DocumentEntity> listDocumentEntity = documentRepo.findAll();
//         for(DocumentEntity d:listDocumentEntity) {
//             System.out.println(d.getCommit().toString());
//             List<SpecItem> list=d.getSpecItems();
//             for (SpecItem s:list) {
//                 System.out.println(s.getShortName());
//             }
//         }
}
