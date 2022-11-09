package amos.specitemdatabase.service;

import amos.specitemdatabase.importer.SpecItemParser;
import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemEntity;
import amos.specitemdatabase.repo.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class SpecItemService {
    private final SpecItemRepo specItemRepo;
    private final SpecItemParser specItemParser = new SpecItemParser();

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo) {
        this.specItemRepo = specItemRepo;
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
//        String filepath = "./tmp/" + filename;
//        File file = new File(filepath);
//        Commit commit, List<SpecItem> specItems = SpecItemParser.splitFileIntoSpecItems(file);
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
