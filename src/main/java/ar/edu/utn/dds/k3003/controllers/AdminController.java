package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.services.IncentivosService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IncentivosService service;

    public AdminController(IncentivosService service) {
        this.service = service;
    }

    @DeleteMapping("/db")
    public void reset() {
        service.reset();
    }
}