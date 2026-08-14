package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.services.IncentivosService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import ar.edu.utn.dds.k3003.scheduler.SchedulerConfig;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IncentivosService service;
    private final InsigniaController insigniaController;
    private final MisionController misionController;
    private final SchedulerConfig schedulerConfig;

    public AdminController(
            IncentivosService service,
            InsigniaController insigniaController,
            MisionController misionController,
            SchedulerConfig schedulerConfig
    ) {
        this.service = service;
        this.insigniaController = insigniaController;
        this.misionController = misionController;
        this.schedulerConfig = schedulerConfig;
    }

    @DeleteMapping("/db")
    public void reset() {
        service.reset();
    }

    @PostMapping("/agregar-misiones-base")
    public String agregarMisionesBase() {
        InsigniaDTO primerosPasos = insigniaController.crear(
                new InsigniaDTO(
                        null,
                        "Primeros pasos",
                        "Reconocimiento por donar productos de distintas categorias"
                )
        );
        InsigniaDTO donadorConstante = insigniaController.crear(
                new InsigniaDTO(
                        null,
                        "Donador constante",
                        "Reconocimiento por alcanzar 20 donaciones exitosas"
                )
        );
        InsigniaDTO impactoCreciente = insigniaController.crear(
                new InsigniaDTO(
                        null,
                        "Impacto creciente",
                        "Reconocimiento por realizar donaciones de impacto creciente"
                )
        );
        InsigniaDTO granBenefactor = insigniaController.crear(
                new InsigniaDTO(
                        null,
                        "Gran benefactor",
                        "Reconocimiento por alcanzar el nivel mas alto de contribucion"
                )
        );
        misionController.crear(
                new MisionDTO(
                        null,
                        "Primeros pasos",
                        primerosPasos.id(),
                        CategoriaDonadorEnum.OCASIONAL,
                        CategoriaDonadorEnum.COLABORADOR,
                        TipoMisionEnum.COMPLETITUD
                )
        );
        misionController.crear(
                new MisionDTO(
                        null,
                        "Donaciones Exitosas",
                        donadorConstante.id(),
                        CategoriaDonadorEnum.COLABORADOR,
                        CategoriaDonadorEnum.TRANSFORMADOR,
                        TipoMisionEnum.DONACIONES_EXITOSAS
                )
        );
        misionController.crear(
                new MisionDTO(
                        null,
                        "Impacto creciente",
                        impactoCreciente.id(),
                        CategoriaDonadorEnum.TRANSFORMADOR,
                        CategoriaDonadorEnum.SALVADOR,
                        TipoMisionEnum.DONACIONES_ASCENDENTES
                )
        );
        misionController.crear(
                new MisionDTO(
                        null,
                        "Gran benefactor",
                        granBenefactor.id(),
                        CategoriaDonadorEnum.SALVADOR,
                        CategoriaDonadorEnum.REVOLUCIONARIO,
                        TipoMisionEnum.REVOLUCION_DONADORA
                )
        );
        return "4 insignias y 4 misiones base agregadas correctamente";
    }

    @PutMapping("/intervalo-job/{segundos}")
    public String cambiarIntervaloJob(@PathVariable long segundos) {
        if (segundos < 1) {
            throw new IllegalArgumentException(
                    "El intervalo debe ser de al menos 1 segundo"
            );
        }
        schedulerConfig.setIntervalo(segundos * 1000);
        return "Intervalo del job cambiado a "
                + segundos
                + " segundos";
    }
}