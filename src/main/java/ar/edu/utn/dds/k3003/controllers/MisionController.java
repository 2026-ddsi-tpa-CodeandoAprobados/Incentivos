package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/misiones")
public class MisionController {

    private Fachada fachada =
            new Fachada();

    @PostMapping
    public MisionDTO crear(
            @RequestBody MisionDTO mision
    ) {

        return fachada.agregarMision(
                mision
        );
    }

    @GetMapping
    public List<MisionDTO> listar() {

        return fachada.getMisiones();
    }

    @GetMapping("/{id}")
    public MisionDTO buscar(
            @PathVariable String id
    ) {

        return fachada.getMision(id);
    }
}