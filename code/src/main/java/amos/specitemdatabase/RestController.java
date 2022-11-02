package amos.specitemdatabase;

import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final Service service;

    @Autowired
    public RestController(Service service) {
        this.service = service;
    }
}
