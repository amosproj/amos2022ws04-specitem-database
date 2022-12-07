package amos.specitemdatabase.service;

import amos.specitemdatabase.config.FileConfig;
import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.importer.SpecItemParserInterface;
import amos.specitemdatabase.model.DocumentEntity;
import amos.specitemdatabase.model.ProcessedDocument;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.Status;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.DocumentRepo;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.tagservice.TagService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpecItemService {
    private final SpecItemRepo specItemRepo;
    private final DocumentRepo documentRepo;
    private final SpecItemParserInterface specItemParser = new SpecItemParser();
    private final FileConfig fileConfig;
    private final TagService tagService;
    private final int MAX_PER_PAGE = 50;

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo, DocumentRepo documentRepo, FileConfig fileConfig,
                           TagService tagService) {
        this.specItemRepo = specItemRepo;
        this.documentRepo = documentRepo;
        this.fileConfig = fileConfig;
        this.tagService = tagService;
    }

    private Pageable getPageableSortedByShortNameInAscendingOrder(int page) {
        return PageRequest.of(page-1, MAX_PER_PAGE, Sort.by(
            "short_name").ascending()
        );
    }

    public List<SpecItem> getSpecItemByContent(String content, int page) {
        Pageable pageable = getPageableSortedByShortNameInAscendingOrder(page);
        List<SpecItem> specItemsList = specItemRepo.findUpdatedSpecItemByContent(content, pageable);
        return specItemsList;
    }

    public List<SpecItem> getAllSpecItems(int page) {
        Pageable pageable = PageRequest.of(page-1, MAX_PER_PAGE, Sort.by("short_name").ascending());
        List<SpecItem> specItems = specItemRepo.fincAllUpdatedSpecitem(pageable);
        return specItems;
    }

    /***
     * save text file as document object and its relating Specitem objects in database
     * @param filename name of the document text file stored in tmp folder
     */
    public void saveDocument(String filename) throws IOException{
        String filepath = fileConfig.getUploadDir() + filename;
        File file = new File(filepath);
        ProcessedDocument pDoc = specItemParser.processFile(file);

        addTags(pDoc.getSpecItems());

        DocumentEntity documentEntity = new DocumentEntity(filename, pDoc.getSpecItems(), pDoc.getCommit());
        documentRepo.save(documentEntity);
        System.out.println(specItemRepo.findAll().size());
    }

    private void addTags(final List<SpecItem> specItems) {
        // Step 1: Fetch current tags
        specItems.forEach(specItem -> {
            final String tags = this.tagService.fetchTags(specItem);
            final TagInfo tagInfo = createTagInfo(specItem, tags);
            specItem.setTagInfo(tagInfo);
        });
    }

    private TagInfo createTagInfo(final SpecItem specItem, final String tags) {
        final TagInfo tagInfo = new TagInfo();
        tagInfo.setShortName(specItem.getShortName());
        tagInfo.setTime(specItem.getTime());
        tagInfo.setStatus(Status.LATEST);
        tagInfo.setTags(tags);
        return tagInfo;
    }

    public void saveTags(final SpecItem taggedSpecItem, final List<String> tags) {
        // TODO: maybe this info will eventually come from the server
        // but for now, it is set manually
        taggedSpecItem.setTime(LocalDateTime.now());
        final TagInfo tagInfo = this.createTagInfo(taggedSpecItem, String.join(", ", tags));
        taggedSpecItem.setTagInfo(tagInfo);
        this.specItemRepo.save(taggedSpecItem);
    }

    public SpecItem getSpecItemById(String specItemId) {
        List<DocumentEntity> listDocumentEntity = documentRepo.findAll();
        //System.out.println(listDocumentEntity);
        SpecItem spec = new SpecItem();
        LocalDateTime base = LocalDateTime.of(1998, 1, 14, 10, 34);
        for(DocumentEntity d:listDocumentEntity) {
            System.out.println(d.getCommit().getCommitTime());

            LocalDateTime dt = d.getCommit().getCommitTime();
            List<SpecItem> list=d.getSpecItems();
            for (SpecItem s:list) {
                if (s.getShortName().equals(specItemId) && dt.isAfter(base)) {
                    System.out.println(s.getShortName());
                    base = dt;
                    spec = s;
                }
            }
        }
        return spec;
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

    public int getPageNumber() {
        int pageNumber = (int) Math.ceil(specItemRepo.getCount() *1.0 / MAX_PER_PAGE) ;
        return pageNumber;
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
