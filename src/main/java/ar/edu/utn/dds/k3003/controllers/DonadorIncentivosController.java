package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donadores")
public class DonadorIncentivosController {

    private final Fachada fachada;

    public DonadorIncentivosController(Fachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping("/{id}/insignias")
    public void asignarInsignia(
            @PathVariable String id,
            @RequestBody InsigniaDTO insignia
    ) {
        fachada.asignarInsigniaADonador(id, insignia);
    }

    @PostMapping("/{id}/misiones")
    public void asignarMision(
            @PathVariable String id,
            @RequestBody MisionDTO mision
    ) {
        fachada.asignarMisionADonador(id, mision);
    }

    @GetMapping("/{id}/insignias")
    public List<InsigniaDTO> obtenerInsignias(
            @PathVariable String id
    ) {
        return fachada.getInsigniasDeDonador(id);
    }

    @GetMapping("/{id}/mision")
    public MisionDTO obtenerMision(
            @PathVariable String id
    ) {
        return fachada.getMisionEnCursoDeDonador(id);
    }

    @PostMapping("/{id}/procesar")
    public void procesar(
            @PathVariable String id
    ) {
        fachada.procesarDonador(id);
    }
}