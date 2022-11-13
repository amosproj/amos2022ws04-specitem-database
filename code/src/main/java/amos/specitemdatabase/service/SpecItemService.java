package amos.specitemdatabase.service;

import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.importer.SpecItemParserInterface;
import amos.specitemdatabase.model.DocumentEntity;
import amos.specitemdatabase.model.ProcessedDocument;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemEntity;
import amos.specitemdatabase.repo.DocumentRepo;
import amos.specitemdatabase.repo.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SpecItemService {
    private final SpecItemRepo specItemRepo;
    private final DocumentRepo documentRepo;
    private final SpecItemParserInterface specItemParser = new SpecItemParser();

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo, DocumentRepo documentRepo) {
        this.specItemRepo = specItemRepo;
        this.documentRepo = documentRepo;
    }

    public ResponseEntity<SpecItemEntity> saveSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
        specItemRepo.save(specItemEntity);
        return ResponseEntity.ok(specItemEntity);
    }
    
    public ResponseEntity<SpecItemEntity> deleteSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
        specItemRepo.delete(specItemEntity);
        return ResponseEntity.ok(specItemEntity);
    }
    
    public ResponseEntity<SpecItemEntity> getSpecItemEntity(@RequestBody long id) {
        for(SpecItemEntity e : specItemRepo.findAll()) {
            if(e.getId().equals(id)) {
                return ResponseEntity.ok(e);
            }
        }
        return null;
    }

    /***
     * save text file as document object and its relating Specitem objects in database
     * @param filename name of the document text file stored in tmp folder
     */
    public void saveDocument(String filename) throws IOException{
        String filepath = "./tmp/" + filename;
        File file = new File(filepath);
        ProcessedDocument pDoc = specItemParser.processFile(file);
//        List<SpecItemEntity> specItemEntities = pDoc.getSpecItems().stream().map(specItemParser::transformSpecItem).collect(Collectors.toList());
        DocumentEntity documentEntity = new DocumentEntity(filename, pDoc.getSpecItems(), pDoc.getCommit());
        documentRepo.save(documentEntity);
    }
    // Test the save method
    // @Bean
    // CommandLineRunner commandLineRunner(
    //     SpecItemRepo specItemRepo
    // ) {
    //     return args -> {
    //         SpecItemEntity specItemEntity = new SpecItemEntity();
    //         specItemEntity.setId(1L);
    //         specItemEntity.setCategory("When I grow up");
    //         specItemEntity.setLcStatus("I want to be an army");
    //         specItemEntity.setLongName("And");
    //         specItemEntity.setContent("Save");
    //         specItemEntity.setCommitHash("Pakistan");
    //         specItemEntity.setVersion(666);
    //         specItemRepo.save(specItemEntity);
    //     };
    // }
}
