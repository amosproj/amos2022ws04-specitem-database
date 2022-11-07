package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItemEntity;
import amos.specitemdatabase.repo.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class SpecItemService {
    private SpecItemRepo specItemRepo;

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo) {
        this.specItemRepo = specItemRepo;
    }

    // @PostMapping("/savespecitem")
    // public ResponseEntity<SpecItemEntity> saveSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
    //     specItemEntity = new SpecItemEntity();
    //     specItemEntity.setId(1L);
    //     System.out.println("Shit happens");
    //     specItemRepo.save(specItemEntity);
    //     return ResponseEntity.ok(specItemEntity);
    // }

    @Bean
    CommandLineRunner commandLineRunner(
        SpecItemRepo specItemRepo
    ) {
        return args -> {
            System.out.println("Shit happens");
            SpecItemEntity specItemEntity = new SpecItemEntity();
            specItemEntity.setId(1L);
            specItemRepo.save(specItemEntity);
        };
    }
}
