package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItemEntity;
import amos.specitemdatabase.repo.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class SpecItemService {
    private final SpecItemRepo specItemRepo;

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo) {
        this.specItemRepo = specItemRepo;
    }

    public ResponseEntity<SpecItemEntity> saveSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
        specItemRepo.save(specItemEntity);
        return ResponseEntity.ok(specItemEntity);
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
