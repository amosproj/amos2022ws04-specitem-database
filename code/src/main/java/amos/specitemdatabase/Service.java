package amos.specitemdatabase;

import amos.specitemdatabase.databaseConnector.SpecItemRepo;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service {
    private final SpecItemRepo specItemRepo;

    @Autowired
    public Service(SpecItemRepo specItemRepo) {
        this.specItemRepo = specItemRepo;
    }
}
