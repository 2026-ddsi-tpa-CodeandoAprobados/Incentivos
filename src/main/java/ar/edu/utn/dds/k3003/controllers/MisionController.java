package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.services.IncentivosService;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.TipoMisionEnum;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    // Cantidad de donaciones ACEPTADAS que pide la mision DONACIONES_EXITOSAS.
    // Por defecto 20; se puede ajustar para testear con menos donaciones.
    @GetMapping("/{id}/cantidad-requerida")
    public int cantidadRequerida(@PathVariable String id) {
        return buscarDonacionesExitosas(id).cantidadRequeridaEfectiva();
    }
    @PutMapping("/{id}/cantidad-requerida/{cantidad}")
    public int configurarCantidadRequerida(
            @PathVariable String id,
            @PathVariable int cantidad
    ) {
        buscarDonacionesExitosas(id);
        if (cantidad < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La cantidad requerida debe ser al menos 1"
            );
        }
        return service.configurarCantidadRequerida(id, cantidad)
                .cantidadRequeridaEfectiva();
    }
    private Mision buscarDonacionesExitosas(String id) {
        Mision mision = service.buscarMision(id);
        if (mision == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Mision no encontrada"
            );
        }
        if (mision.getTipo() != TipoMisionEnum.DONACIONES_EXITOSAS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo las misiones DONACIONES_EXITOSAS tienen cantidad configurable"
            );
        }
        return mision;
    }
}