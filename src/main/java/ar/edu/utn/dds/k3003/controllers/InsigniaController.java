package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import org.springframework.web.bind.annotation.*;
import ar.edu.utn.dds.k3003.services.IncentivosService;

import java.util.List;

@RestController
@RequestMapping("/insignias")
public class InsigniaController {

    private final Fachada fachada;
    private final IncentivosService service;

    public InsigniaController(
            Fachada fachada,
            IncentivosService service
    ) {
        this.fachada = fachada;
        this.service = service;
    }

    @PostMapping
    public InsigniaDTO crear(@RequestBody InsigniaDTO insignia) {
        return fachada.agregarInsignia(insignia);
    }

    @GetMapping
    public List<InsigniaDTO> listar() {
        return fachada.getInsignias();
    }

    @GetMapping("/{id}")
    public InsigniaDTO buscar(@PathVariable String id) {
        return fachada.getInsignia(id);
    }

@DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminarInsignia(id);
    }
}

