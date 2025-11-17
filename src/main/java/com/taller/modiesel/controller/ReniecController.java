package com.taller.modiesel.controller;

import com.taller.modiesel.external.ReniecApiClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reniec")
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
