package amos.specitemdatabase.service;

import amos.specitemdatabase.config.FileConfig;
import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.importer.SpecItemParserInterface;
import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.CompareResult;
import amos.specitemdatabase.model.CompareResultMarkup;
import amos.specitemdatabase.model.DocumentEntity;
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
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
        System.out.println(content);
        List<SpecItem> listOfSpecItems = specItemRepo.findUpdatedSpecItemByContent(content, pageable);
        return listOfSpecItems;
    }

    @Transactional
    public List<SpecItem> getListOfSpecItemsByIDAndContent(String shortName, String content) {
        return specItemRepo.findAllByShortNameAndContentContaining(shortName, content);
    }

    @Transactional
    public List<SpecItem> getAllSpecItems(int page) {
        Pageable pageable = getPageableSortedByShortNameInAscendingOrder(page);
        List<SpecItem> listOfSpecItems = specItemRepo.findAllUpdatedSpecitem(pageable);
        return listOfSpecItems;
    }

    @Transactional
    public SpecItem getSpecItemById(String specItemId) {
        try {
            SpecItem specItem =
                specItemRepo.findFirstByShortNameContainingOrderByCommitTimeDesc(specItemId);
            log.info("Fetched the spec item with time: {} and ID: {} and tags: {}", specItem.getCommitTime(),
                specItem.getShortName(), specItem.getTagInfo().getTags());
            return specItem;
        } catch (NullPointerException npe) {
            log.info("Referencing trace ref with ID={} which is not in the DB.", specItemId);
            return null;
        }
    }

    @Transactional
    public List<SpecItem> getListOfSpecItemsById(String specItemId) {
        List<SpecItem> listOfSpecItems = specItemRepo.getAllVersionsOfASpecItemByID(specItemId);
        return listOfSpecItems;
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

    private SpecItem newVersionOfDeletedSpecItem(final SpecItem taggedSpecItem) {
        final SpecItem newVersionOfSpecItem = prepareBaseForNewVersionOfSpecItem(taggedSpecItem);
        newVersionOfSpecItem.setTraceRefs(new ArrayList<>(taggedSpecItem.getTraceRefs()));
        newVersionOfSpecItem.setUseInstead(taggedSpecItem.getUseInstead());
        newVersionOfSpecItem.setLongName(taggedSpecItem.getLongName());
        newVersionOfSpecItem.setContent(taggedSpecItem.getContent());
        newVersionOfSpecItem.setStatus(taggedSpecItem.getStatus());
        newVersionOfSpecItem.setMarkedAsDeleted(true);
        return newVersionOfSpecItem;
    }

    private SpecItem prepareBaseForNewVersionOfSpecItem(final SpecItem taggedSpecItem) {
        final SpecItem newVersionOfSpecItem = new SpecItem();
        //newVersionOfSpecItem.setCommitTime(LocalDateTime.now());
        newVersionOfSpecItem.setCreationTime(taggedSpecItem.getCreationTime());
        newVersionOfSpecItem.setShortName(taggedSpecItem.getShortName());
        newVersionOfSpecItem.setFingerprint(taggedSpecItem.getFingerprint());
        newVersionOfSpecItem.setCategory(taggedSpecItem.getCategory());
        newVersionOfSpecItem.setLcStatus(taggedSpecItem.getLcStatus());
        return newVersionOfSpecItem;
    }

    @Transactional
    public void deleteSpecItemById(String specItemId) {
        try {
            createNewVersionOfDeletedSpecItem(specItemId);
        } catch (Exception lockingFailureException) {
            System.err.println(lockingFailureException.getMessage());
        }
    }

    @Transactional
    public int getPageNumber() {
        return (int) Math.ceil(specItemRepo.getCount()*1.0/MAX_PER_PAGE);
    }

    @Transactional
    public void saveDocument(String filename) throws IOException{
        String filepath = fileConfig.getUploadDir() + filename;
        ProcessedDocument pDoc = specItemParser.processFile(new File(filepath));

        addTags(pDoc.getSpecItems());

        DocumentEntity documentEntity = new DocumentEntity(filename, pDoc.getSpecItems(), pDoc.getCommit());
        documentRepo.save(documentEntity);
    }

    @Transactional
    public void completeTagAdditionProcess(final SpecItem taggedSpecItem, final List<String> newTags) {
    private TagInfo createTagInfo(final SpecItem specItem, final String tags, boolean isGuiUpdate) {
        String allTags;
        if (!isGuiUpdate) {
            final String previousTags = this.tagService.fetchTags(specItem);
            log.debug("The previous tags of the SpecItem are: {}", previousTags);
            allTags = previousTags + tags;
        } else {
            allTags = tags;
        }
        final TagInfo tagInfo = new TagInfo();
        tagInfo.setShortName(specItem.getShortName());
        tagInfo.setCommitTime(specItem.getCommitTime());
        tagInfo.setStatus(Status.LATEST);
        tagInfo.setTags(allTags);
        return tagInfo;
    }


    public boolean completeTagAdditionProcess(final SpecItem taggedSpecItem, final String newTags)
        throws InterruptedException {
        final LocalDateTime newCommitTime = LocalDateTime.now();
        boolean tagsAdded;
        try {
            log.info("Saving the tags: {} for SpecItem with ID:{} and CommitTime: {}",
                newTags, taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime());
            this.tagService.saveTags(
                taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime(), newTags);
            tagsAdded = true;
        } catch (ObjectOptimisticLockingFailureException lockingFailureException) {
            log.info("There was a concurrent update. The new version will be saved.");
            // 1. Wait a bit
            Thread.sleep(2000);
            // 2. Get the tags for the item that caused the locking (ID, Old)
            final TagInfo currentTagOfTaggedSpecItem = this.tagService.getTagsBySpecItemIdAndCommitTime(
                taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime());
            // 3. Save the tag info of the new version as the version has been increased,
            // and it is not possible to save under the same primary key
            String allTags = currentTagOfTaggedSpecItem + ", " + newTags;
            tagsAdded = this.tagService.saveTagsWithNewCommitTime(
                    taggedSpecItem.getShortName(), newCommitTime, allTags);
        }
        // maybe here is the problem with the time, check if they are not the same for concurrent updates
        this.createAndSaveNewVersion(taggedSpecItem, newCommitTime);
        return tagsAdded;
    }

    private void createAndSaveNewVersion(final SpecItem taggedSpecItem, final LocalDateTime now) {
        final Commit c = new Commit(
            "hash", "message", now, "author");
        final SpecItem newVersionOfSpecItem = this.prepareNewVersionOfSpecItem(taggedSpecItem);
        newVersionOfSpecItem.setCommit(c);
        newVersionOfSpecItem.setCommitTime(c.getCommitTime());
        final TagInfo tagInfo = this.tagService.getTagsBySpecItemIdAndCommitTime(
            taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime()
        );
        final TagInfo newTagInfo = new TagInfo();
        newTagInfo.setCommitTime(now);
        newTagInfo.setShortName(taggedSpecItem.getShortName());
        newTagInfo.setTags(tagInfo.getTags());
        newVersionOfSpecItem.setTagInfo(newTagInfo);
        log.info("Creating a new version of the SpecItem with the ID: {} and CommitTime: {}" +
                " with the new tags: {}", newVersionOfSpecItem.getShortName(),
            newVersionOfSpecItem.getCommitTime(), newVersionOfSpecItem.getTagInfo().getTags());
        final DocumentEntity documentEntity = new DocumentEntity("filename", List.of(newVersionOfSpecItem), c);
        documentRepo.save(documentEntity);
    }

    private String fetchCurrentTags(final SpecItem taggedSpecItem, final List<String> newTags) {
        final TagInfo previousTagInfo = this.tagService.getTagsBySpecItemIdAndCommitTime(
            taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime());
        String allTags;
        if (previousTagInfo != null) {
            final String previousTags = previousTagInfo.getTags();
            log.info("The already existing tags for ID={} CommitTime={} are {}",
                taggedSpecItem.getShortName(), taggedSpecItem.getCommitTime(), previousTags);
            if (previousTags.isEmpty()) {
                allTags = String.join(",", newTags);
            } else {
                allTags = previousTags + "," + String.join(",", newTags);
            }
        } else {
            allTags = String.join(",", newTags);
        }
        return allTags;
    }

    private SpecItem prepareNewVersionOfSpecItem(final SpecItem taggedSpecItem) {
        final SpecItem newVersionOfSpecItem = prepareBaseForNewVersionOfSpecItem(taggedSpecItem);
        newVersionOfSpecItem.setTraceRefs(taggedSpecItem.getTraceRefs());
        newVersionOfSpecItem.setUseInstead(taggedSpecItem.getUseInstead());
        newVersionOfSpecItem.setLongName(taggedSpecItem.getLongName());
        newVersionOfSpecItem.setContent(taggedSpecItem.getContent());
        newVersionOfSpecItem.setStatus(taggedSpecItem.getStatus());
        newVersionOfSpecItem.setMarkedAsDeleted(false);
        return newVersionOfSpecItem;
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
//
//            Commit commit2 = new Commit(
//                    "hash",
//                    "message",
//                    LocalDateTime.of(2019, 03, 28, 14, 33, 48, 640000),
//                    "author"
//                );
//
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
//
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
//
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
//
//            List<SpecItem> specItems = new ArrayList<>();
//            specItems.add(specItem);
//            specItems.add(specItem3);
//            DocumentEntity documentEntity = new DocumentEntity("name",specItems,commit);
//            documentRepo.save(documentEntity);
//
//            List<SpecItem> specItems2 = new ArrayList<>();
//            specItems2.add(specItem2);
//            DocumentEntity documentEntity2 = new DocumentEntity("name2",specItems2,commit2);
//            documentRepo.save(documentEntity2);
//
//
//            // List<String> tags = List.of("Key1:Value1","Key2:Value2");
//            // specItems.forEach(specs -> saveTags(specs, tags));
//
//            //this.deleteSpecItemById(specItem.getShortName(), documentEntity.getName());
//        };
//    }
}
