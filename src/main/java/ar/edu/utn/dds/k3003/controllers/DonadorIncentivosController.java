package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.dds.k3003.services.IncentivosService;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.dtos.DonadorIncentivosDetalleDTO;

import java.util.List;

@RestController
@RequestMapping("/incentivos-donador")
public class DonadorIncentivosController {

    private final Fachada fachada;
    private final IncentivosService service;

    public DonadorIncentivosController(
            Fachada fachada,
            IncentivosService service
    ) {
        this.fachada = fachada;
        this.service = service;
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

    @PostMapping("/{id}")
    public void registrarDonador(
            @PathVariable String id
    ) {
        service.registrarNuevoDonador(id);
    }

    @GetMapping
    public List<DonadorIncentivosDetalleDTO> obtenerTodosLosDonadores() {

        return service.obtenerTodosLosDonadores()
                .stream()
                .map(this::toDetalleDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public DonadorIncentivosDetalleDTO obtenerDetalleDonador(
            @PathVariable String id
    ) {
        DonadorIncentivos donador =
                service.obtenerDonador(id);

        return toDetalleDTO(donador);
    }



    private DonadorIncentivosDetalleDTO toDetalleDTO(
            DonadorIncentivos donador
    ) {

        MisionDTO misionEnCurso = null;

        if (donador.getMisionEnCurso() != null) {
            misionEnCurso =
                    convertirMision(donador.getMisionEnCurso());
        }

        List<MisionDTO> misionesCompletadas =
                donador.getMisionesCompletadas()
                        .stream()
                        .map(this::convertirMision)
                        .toList();

        List<InsigniaDTO> insignias =
                donador.getInsignias()
                        .stream()
                        .map(i -> new InsigniaDTO(
                                i.getId(),
                                i.getNombre(),
                                i.getDescripcion()
                        ))
                        .toList();

        return new DonadorIncentivosDetalleDTO(
                donador.getId(),
                misionEnCurso,
                misionesCompletadas,
                insignias
        );
    }

    private MisionDTO convertirMision(Mision mision) {

        return new MisionDTO(
                mision.getId(),
                mision.getNombre(),
                mision.getInsigniaID(),
                mision.getCategoriaInicio(),
                mision.getCategoriaFin(),
                ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum
                        .valueOf(mision.getTipo().name())
        );
    }


}
