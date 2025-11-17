package com.taller.modiesel.controller;

import com.taller.modiesel.external.ReniecApiClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/reniec", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class ReniecController {

    private final ReniecApiClient reniecApiClient;

    public ReniecController(ReniecApiClient reniecApiClient) {
        this.reniecApiClient = reniecApiClient;
    }

    @GetMapping("/consultar/{ruc}")
    public String consultarRuc(@PathVariable String ruc) {
        return reniecApiClient.consultarRuc(ruc);
    }
}
