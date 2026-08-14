package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.services.IncentivosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/misiones")
public class MisionController {

    private final Fachada fachada;
    private final IncentivosService service;

    public MisionController(
            Fachada fachada,
            IncentivosService service
    ) {
        this.fachada = fachada;
        this.service = service;
    }
    @PostMapping
    public MisionDTO crear(@RequestBody MisionDTO mision) {
        return fachada.agregarMision(mision);
    }
    @GetMapping
    public List<MisionDTO> listar() {
        return fachada.getMisiones();
    }
    @GetMapping("/{id}")
    public MisionDTO buscar(@PathVariable String id) {
        return fachada.getMision(id);
    }
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable String id) {
        service.eliminarMision(id);
    }
}