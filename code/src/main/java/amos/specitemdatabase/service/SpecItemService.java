package amos.specitemdatabase.service;

import amos.specitemdatabase.config.FileConfig;
import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.importer.SpecItemParserInterface;
import amos.specitemdatabase.model.Category;
import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.CompareResult;
import amos.specitemdatabase.model.CompareResultMarkup;
import amos.specitemdatabase.model.DocumentEntity;
import amos.specitemdatabase.model.LcStatus;
import amos.specitemdatabase.model.ProcessedDocument;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemId;
import amos.specitemdatabase.model.Status;
import amos.specitemdatabase.model.TagInfo;
import amos.specitemdatabase.repo.DocumentRepo;
import amos.specitemdatabase.repo.SpecItemRepo;
import amos.specitemdatabase.tagservice.TagService;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
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

    private Commit getCommitWithCurrentTime() {
        final LocalDateTime dateTime = LocalDateTime.now();
        Commit commit = new Commit("hash"+ dateTime.toString(),"message"+ dateTime.toString(),dateTime,"author"+ dateTime.toString());
        return commit;
    }

    private String joinPreviousAndCurrentTags(TagInfo previousTagInfo, List<String> newTags) {
        String previousTags = previousTagInfo.getTags();
        return previousTags.isEmpty() ? String.join(",", newTags) : previousTags + "," + String.join(",", newTags);
    }

    private String fetchCurrentTags(final SpecItem taggedSpecItem, final List<String> newTags) {
        final TagInfo previousTagInfo = this.tagService.getTagsBySpecItemIdAndCommitTime(taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime());
        String allTags = tagNullCheck(previousTagInfo, newTags);
        return allTags;
    }
    
    private String tagNullCheck(TagInfo previousTagInfo) {
        return previousTagInfo != null ? previousTagInfo.getTags() : "";
    }
    
    private String tagNullCheck(TagInfo previousTagInfo, List<String> newTags) {
        return previousTagInfo != null ? joinPreviousAndCurrentTags(previousTagInfo, newTags)  : String.join(",", newTags);
    }

    private String getTagsFromPreviousVersion(SpecItem latestSpecItem) {
        final TagInfo previousTagInfo = this.tagService.getTagsBySpecItemIdAndCommitTime(latestSpecItem.getShortName(), latestSpecItem.getCommitTime());
        String allTags = tagNullCheck(previousTagInfo);
        return allTags;
    }

    private TagInfo getTagInfoFromLatestVersionOfASpecItem(SpecItem latestSpecItem, SpecItem specItemMarkedAsDeleted, boolean isGuiUpdate) {
        String allTags = getTagsFromPreviousVersion(latestSpecItem);
        final TagInfo tagInfo = this.createTagInfo(specItemMarkedAsDeleted, String.join(", ", allTags), isGuiUpdate);
        return tagInfo;
    }

    private void saveSpecItemViaDocument(SpecItem specItemMarkedAsDeleted, Commit commit) {
        DocumentEntity documentEntity = new DocumentEntity("document_for_updated_versions_of_a_specitem", Arrays.asList(specItemMarkedAsDeleted), commit);
        documentRepo.save(documentEntity);
    }

    private void createNewVersionOfDeletedSpecItem(String specItemId, boolean markedAsDeleted, boolean isGuiUpdate) {
        SpecItem latestSpecItem = specItemRepo.getLatestSpecItemByID(specItemId);
        SpecItem newVersionOfTheSpecItem = prepareNewVersionOfSpecItem(latestSpecItem, markedAsDeleted);

        Commit commit = getCommitWithCurrentTime();
        newVersionOfTheSpecItem.setCommit(commit);

        TagInfo tagInfo = getTagInfoFromLatestVersionOfASpecItem(latestSpecItem, newVersionOfTheSpecItem, isGuiUpdate);
        newVersionOfTheSpecItem.setTagInfo(tagInfo);

        saveSpecItemViaDocument(newVersionOfTheSpecItem,commit);
    }

    // public void saveDocumentWithTag(String filename, List<SpecItem> sp, Commit c, final List<String> tags) {
    //     final TagInfo tagInfo = this.createTagInfo(sp.get(0), String.join(", ", tags), false);
    //     sp.get(0).setTagInfo(tagInfo);

    //     DocumentEntity documentEntity = new DocumentEntity(filename, sp, c);
    //     documentRepo.save(documentEntity);
    // }

    private String saveTagsToTable(SpecItem taggedSpecItem, final List<String> newTags) {
        final String allTags = fetchCurrentTags(taggedSpecItem, newTags);
        System.out.println(allTags);
        this.tagService.saveTags(taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime(), allTags);
        return allTags;
    }

    @Transactional
    public void completeTagAdditionProcess(final SpecItem taggedSpecItem, final List<String> newTags) {
        try {
            saveTagsToTable(taggedSpecItem, newTags);

            createNewVersionOfDeletedSpecItem(taggedSpecItem.getShortName(), false, false);
        } catch (Exception lockingFailureException) {
            log.warn("Somebody has just updated the tags for the SpecItem " +
                "with the ID: {}. Retrying...", taggedSpecItem.getShortName());
            this.completeTagAdditionProcess(taggedSpecItem, newTags);
        }
    }
    
    @Transactional
    public void deleteSpecItemById(String specItemId) {
        try {
            createNewVersionOfDeletedSpecItem(specItemId, true, true);           
        } catch (Exception lockingFailureException) {
            System.err.println(lockingFailureException.getMessage());
        }
    }

    public int getPageNumber() {
        return (int) Math.ceil(specItemRepo.getCount()*1.0/MAX_PER_PAGE);
    }

    private void addTags(final List<SpecItem> specItems) {
        specItems.forEach(specItem -> {
            final String tags = this.tagService.fetchTags(specItem);
            final TagInfo tagInfo = createTagInfo(specItem, tags, false);
            specItem.setTagInfo(tagInfo);
        });
    }

    @Transactional
    public void saveDocument(String filename) throws IOException{
        String filepath = fileConfig.getUploadDir() + filename;
        ProcessedDocument pDoc = specItemParser.processFile(new File(filepath));

        addTags(pDoc.getSpecItems());

        DocumentEntity documentEntity = new DocumentEntity(filename, pDoc.getSpecItems(), pDoc.getCommit());
        documentRepo.save(documentEntity);
    }

    private String getAllTagsAndCheckForGUIUpdate(SpecItem specItem, boolean isGuiUpdate, String tags) {
        return isGuiUpdate ? tags : this.tagService.fetchTags(specItem) + tags;
    }

    private TagInfo createTagInfo(final SpecItem specItem, final String tags, boolean isGuiUpdate) {
        String allTags = getAllTagsAndCheckForGUIUpdate(specItem, isGuiUpdate, tags);
        final TagInfo tagInfo = new TagInfo();
        tagInfo.setShortName(specItem.getShortName());
        tagInfo.setCommitTime(specItem.getCommitTime());
        tagInfo.setStatus(Status.LATEST);
        tagInfo.setTags(allTags);
        return tagInfo;
    }

    private SpecItem prepareNewVersionOfSpecItem(final SpecItem taggedSpecItem, boolean markedAsDeleted) {
        final SpecItem newVersionOfSpecItem = new SpecItem();
        newVersionOfSpecItem.setCommitTime(LocalDateTime.now());
        newVersionOfSpecItem.setCreationTime(taggedSpecItem.getCreationTime());
        newVersionOfSpecItem.setShortName(taggedSpecItem.getShortName());
        newVersionOfSpecItem.setFingerprint(taggedSpecItem.getFingerprint());
        newVersionOfSpecItem.setCategory(taggedSpecItem.getCategory());
        newVersionOfSpecItem.setLcStatus(taggedSpecItem.getLcStatus());
        newVersionOfSpecItem.setTraceRefs(new ArrayList<>(taggedSpecItem.getTraceRefs()));
        newVersionOfSpecItem.setUseInstead(taggedSpecItem.getUseInstead());
        newVersionOfSpecItem.setLongName(taggedSpecItem.getLongName());
        newVersionOfSpecItem.setContent(taggedSpecItem.getContent());
        newVersionOfSpecItem.setStatus(taggedSpecItem.getStatus());
        newVersionOfSpecItem.setMarkedAsDeleted(markedAsDeleted);
        return newVersionOfSpecItem;
    }

    // Time bug?

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

    // Here too?

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
    
    @Deprecated
    private void deleteSpecItemFromDocument(DocumentEntity documentEntity, SpecItem specItem) {
        documentEntity.getSpecItems().remove(specItem);
        documentRepo.save(documentEntity);
    }

    @Deprecated
    private void deleteLinkBetweenDocumentAndSpecItem(DocumentEntity documentEntity, String specItemID) {
        for (SpecItem specItem : documentEntity.getSpecItems()) {
            if (specItem.getShortName().equals(specItemID)) {
                deleteSpecItemFromDocument(documentEntity, specItem);
            }
        }
    }

    @Deprecated
    @Transactional
    public void deleteSpecItemByIdInDocument(String specItemId, BigInteger documentId) {
        DocumentEntity documentEntity = documentRepo.getDocumentEntityByID(documentId);
        this.deleteLinkBetweenDocumentAndSpecItem(documentEntity, specItemId);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(
//        DocumentRepo documentRepo
//    ) {
//        return args -> {
//            Commit commit = new Commit(
//                "hash",
//                "message",
//                LocalDateTime.now(),
//                "author"
//            );

//            Commit commit2 = new Commit(
//                    "hash",
//                    "message",
//                    LocalDateTime.of(2019, 03, 28, 14, 33, 48, 640000),
//                    "author"
//                );

//            SpecItem specItem = new SpecItem();
//            specItem.setShortName("ID1");
//            specItem.setContent("content");
//            specItem.setCommit(commit);
//            specItem.setFingerprint("fingerprint");
//            specItem.setLongName("longName");
//            specItem.setUseInstead("useInstead");
//            specItem.setTraceRefs(new LinkedList<>());
//            specItem.setCommitTime(commit.getCommitTime());
//            specItem.setCategory(Category.CATEGORY1);
//            specItem.setLcStatus(LcStatus.STATUS1);

//            SpecItem specItem2 = new SpecItem();
//            specItem2.setShortName("ID1");
//            specItem2.setContent("content2");
//            specItem2.setCommit(commit2);
//            specItem2.setFingerprint("fingerprint");
//            specItem2.setLongName("longName");
//            specItem2.setUseInstead("useInstead");
//            specItem2.setTraceRefs(new LinkedList<>());
//            specItem2.setCommitTime(commit2.getCommitTime());
//            specItem2.setCategory(Category.CATEGORY1);
//            specItem2.setLcStatus(LcStatus.STATUS1);

//            SpecItem specItem3 = new SpecItem();
//            specItem3.setShortName("ID3");
//            specItem3.setContent("content");
//            specItem3.setCommit(commit);
//            specItem3.setFingerprint("fingerprint");
//            specItem3.setLongName("longName");
//            specItem3.setUseInstead("useInstead");
//            specItem3.setTraceRefs(new LinkedList<>());
//            specItem3.setCommitTime(commit.getCommitTime());
//            specItem3.setCategory(Category.CATEGORY1);
//            specItem3.setLcStatus(LcStatus.STATUS1);

//            List<SpecItem> specItems = new ArrayList<>();
//            specItems.add(specItem);
//            specItems.add(specItem3);
//            DocumentEntity documentEntity = new DocumentEntity("name",specItems,commit);
//            documentRepo.save(documentEntity);

//            List<SpecItem> specItems2 = new ArrayList<>();
//            specItems2.add(specItem2);
//            DocumentEntity documentEntity2 = new DocumentEntity("name2",specItems2,commit2);
//            documentRepo.save(documentEntity2);


//            // List<String> tags = List.of("Key1:Value1","Key2:Value2");
//            // specItems.forEach(specs -> saveTags(specs, tags));

//            //this.deleteSpecItemById(specItem.getShortName(), documentEntity.getName());
//        };
//    }
}
