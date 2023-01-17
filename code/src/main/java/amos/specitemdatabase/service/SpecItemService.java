package amos.specitemdatabase.service;

import amos.specitemdatabase.config.FileConfig;
import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.importer.SpecItemParserInterface;
import amos.specitemdatabase.model.*;
import amos.specitemdatabase.repo.DocumentRepo;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.tagservice.TagService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedList;
import java.util.Optional;


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

    public List<SpecItem> getListOfSpecItemsByContent(String content, int page) {
        Pageable pageable = getPageableSortedByShortNameInAscendingOrder(page);
        content = content.replaceAll("%", "\\\\%");
        System.out.println(content);
        List<SpecItem> listOfSpecItems = specItemRepo.findUpdatedSpecItemByContent(content, pageable);
        return listOfSpecItems;
    }

    public List<SpecItem> getListOfSpecItemsByIDAndContent(String shortName, String content) {
        return specItemRepo.findAllByShortNameAndContentContaining(shortName, content);
    }

    public List<SpecItem> getAllSpecItems(int page) {
        Pageable pageable = getPageableSortedByShortNameInAscendingOrder(page);
        List<SpecItem> listOfSpecItems = specItemRepo.findAllUpdatedSpecitem(pageable);
        return listOfSpecItems;
    }

    public SpecItem getSpecItemById(String specItemId) {
        SpecItem specItem = specItemRepo.getLatestSpecItemByID(specItemId);
        return specItem;
    }

    public List<SpecItem> getListOfSpecItemsById(String specItemId) {
        List<SpecItem> listOfSpecItems = specItemRepo.getAllVersionsOfASpecItemByID(specItemId);
        return listOfSpecItems;
    }

    private void deleteSpecItemFromDocument(DocumentEntity documentEntity, SpecItem specItem) {
        documentEntity.getSpecItems().remove(specItem);
        documentRepo.save(documentEntity);
    }

    private void deleteLinkBetweenDocumentAndSpecItem(DocumentEntity documentEntity, String specItemID) {
        for (SpecItem specItem : documentEntity.getSpecItems()) {
            if (specItem.getShortName().equals(specItemID)) {
                deleteSpecItemFromDocument(documentEntity, specItem);
            }
        }
    }
    
    @Transactional
    public void deleteSpecItemById(String specItemId, String documentID) {
        DocumentEntity documentEntity = documentRepo.getDocumentEntityByID(documentID);
        this.deleteLinkBetweenDocumentAndSpecItem(documentEntity, specItemId);
    }

    public int getPageNumber() {
        int pageNumber = (int) Math.ceil(specItemRepo.getCount()*1.0/MAX_PER_PAGE);
        return pageNumber;
    }

    /***
     * save text file as document object and its relating Specitem objects in database
     * @param filename name of the document text file stored in tmp folder
     */
    @Transactional
    public void saveDocument(String filename) throws IOException{
        String filepath = fileConfig.getUploadDir() + filename;
        File file = new File(filepath);
        ProcessedDocument pDoc = specItemParser.processFile(file);

        addTags(pDoc.getSpecItems());

        DocumentEntity documentEntity = new DocumentEntity(filename, pDoc.getSpecItems(), pDoc.getCommit());
        documentRepo.save(documentEntity);
        System.out.println(specItemRepo.findAll().size());
    }
    public void saveDocumentWithTag(String filename, List<SpecItem> sp, Commit c, final List<String> tags) throws IOException{

        //addTags(sp);
        final TagInfo tagInfo = this.createTagInfo(sp.get(0), String.join(", ", tags));
        sp.get(0).setTagInfo(tagInfo);
        DocumentEntity documentEntity = new DocumentEntity(filename, sp, c);
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
        final SpecItem newVersionOfSpecItem = new SpecItem();
        newVersionOfSpecItem.setTime(LocalDateTime.now());
        newVersionOfSpecItem.setShortName(taggedSpecItem.getShortName());
        newVersionOfSpecItem.setFingerprint(taggedSpecItem.getFingerprint());
        newVersionOfSpecItem.setCategory(taggedSpecItem.getCategory());
        newVersionOfSpecItem.setLcStatus(taggedSpecItem.getLcStatus());
        newVersionOfSpecItem.setTraceRefs(taggedSpecItem.getTraceRefs());
        newVersionOfSpecItem.setUseInstead(taggedSpecItem.getUseInstead());
        newVersionOfSpecItem.setLongName(taggedSpecItem.getLongName());
        newVersionOfSpecItem.setContent(taggedSpecItem.getContent());
        newVersionOfSpecItem.setStatus(taggedSpecItem.getStatus());
        final TagInfo tagInfo = this.createTagInfo(newVersionOfSpecItem, String.join(", ", tags));
        newVersionOfSpecItem.setTagInfo(tagInfo);
        this.specItemRepo.save(newVersionOfSpecItem);
    }

    public List<CompareResult> compare(String shortName, LocalDateTime timeOld, LocalDateTime timeNew) throws IllegalAccessException {
        Optional<SpecItem> optionalsOld = specItemRepo.findById(new SpecItemId(shortName, timeOld));
        Optional<SpecItem> optionalsNew = specItemRepo.findById(new SpecItemId(shortName, timeNew));
        if(optionalsOld.isEmpty() || optionalsNew.isEmpty()) {
            throw new IllegalArgumentException("Specitems not in database!");
        }
        SpecItem sOld = optionalsOld.get();
        SpecItem sNew = optionalsNew.get();
        return SpecitemsComparator.compare(sOld, sNew);
    }

    public List<CompareResultMarkup> compareMarkup(String shortName, LocalDateTime timeOld, LocalDateTime timeNew) throws IllegalAccessException {
        Optional<SpecItem> optionalsOld = specItemRepo.findById(new SpecItemId(shortName, timeOld));
        Optional<SpecItem> optionalsNew = specItemRepo.findById(new SpecItemId(shortName, timeNew));
        if(optionalsOld.isEmpty() || optionalsNew.isEmpty()) {
            throw new IllegalArgumentException("Specitems not in database!");
        }
        SpecItem sOld = optionalsOld.get();
        SpecItem sNew = optionalsNew.get();
        return SpecitemsComparator.compareMarkup(sOld, sNew);
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
//             SpecItem specItem = new SpecItem();
//             specItem.setShortName("ID1");
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
//             specItem2.setShortName("ID1");
//             specItem2.setContent("content2");
//             specItem2.setCommit(commit2);
//             specItem2.setFingerprint("fingerprint");
//             specItem2.setLongName("longName");
//             specItem2.setUseInstead("useInstead");
//             specItem2.setTraceRefs(new LinkedList<>());
//             specItem2.setTime(commit2.getCommitTime());
//             specItem2.setCategory(Category.CATEGORY1);
//             specItem2.setLcStatus(LcStatus.STATUS1);
//
//             SpecItem specItem3 = new SpecItem();
//             specItem3.setShortName("ID3");
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
//             List<SpecItem> specItems = new ArrayList<>();
//             specItems.add(specItem);
//             specItems.add(specItem3);
//             DocumentEntity documentEntity = new DocumentEntity("name",specItems,commit);
//             documentRepo.save(documentEntity);
//
//             List<SpecItem> specItems2 = new ArrayList<>();
//             specItems2.add(specItem2);
//             DocumentEntity documentEntity2 = new DocumentEntity("name2",specItems2,commit2);
//             documentRepo.save(documentEntity2);
//            
//            
//             List<String> tags = List.of("Key1:Value1","Key2:Value2");
//             specItems.forEach(specs -> saveTags(specs, tags));
//
//             this.deleteSpecItemById(specItem.getShortName(), documentEntity.getName());
//         };
//     }
}
