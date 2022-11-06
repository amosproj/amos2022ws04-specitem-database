package amos.specitemdatabase.controller;

import amos.specitemdatabase.service.SpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final SpecItemService service;

    @Autowired
    public Controller(SpecItemService service) {
        this.service = service;
    }
}
