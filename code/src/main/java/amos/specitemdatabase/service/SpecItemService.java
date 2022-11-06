package amos.specitemdatabase.service;

import amos.specitemdatabase.repo.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SpecItemService {
    private final SpecItemRepo specItemRepo;

    @Autowired
    public SpecItemService(SpecItemRepo specItemRepo) {
        this.specItemRepo = specItemRepo;
    }
}
