package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/insignias")
public class InsigniaController {

    private Fachada fachada =
            new Fachada();

    @PostMapping
    public InsigniaDTO crear(
            @RequestBody InsigniaDTO insignia
    ) {

        return fachada.agregarInsignia(
                insignia
        );
    }

    @GetMapping
    public List<InsigniaDTO> listar() {

        return fachada.getInsignias();
    }

    @GetMapping("/{id}")
    public InsigniaDTO buscar(
            @PathVariable String id
    ) {

        return fachada.getInsignia(id);
    }
}