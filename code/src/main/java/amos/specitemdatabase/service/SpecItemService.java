package amos.specitemdatabase.service;

import amos.specitemdatabase.model.SpecItemEntity;
import amos.specitemdatabase.repo.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("\\saveSpecItem")
    public ResponseEntity<SpecItemEntity> saveSpecItemEntity(@RequestBody SpecItemEntity specItemEntity) {
        specItemRepo.save(specItemEntity);
        return ResponseEntity.ok(specItemEntity);
    }
}
