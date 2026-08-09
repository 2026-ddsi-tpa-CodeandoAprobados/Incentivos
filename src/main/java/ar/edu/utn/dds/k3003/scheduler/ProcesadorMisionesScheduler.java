package ar.edu.utn.dds.k3003.scheduler;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.DonacionesClient;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.services.IncentivosService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcesadorMisionesScheduler {

    private final DonadorRepository donadorRepository;
    private final DonacionesClient donacionesClient;
    private final IncentivosService incentivosService;

    public ProcesadorMisionesScheduler(
            DonadorRepository donadorRepository,
            DonacionesClient donacionesClient,
            IncentivosService incentivosService
    ) {
        this.donadorRepository = donadorRepository;
        this.donacionesClient = donacionesClient;
        this.incentivosService = incentivosService;
    }

    @Scheduled(
            fixedDelayString = "${incentivos.scheduler.intervalo:60000}"
    )
    public void procesarMisiones() {
        List<DonadorIncentivos> donadores =
                donadorRepository.findAll();

        for (DonadorIncentivos donador : donadores) {
            try {
                boolean tieneMisionEnCurso =
                        donador.getMisionEnCurso() != null;

                boolean tieneMisionesCompletadas =
                        !donador.getMisionesCompletadas().isEmpty();
                if (!tieneMisionEnCurso &&
                        !tieneMisionesCompletadas) {
                    continue;
                }
                List<DonacionDTO> donaciones =
                        donacionesClient.buscarPorDonador(
                                donador.getId()
                        );
                incentivosService.procesarDonador(
                        donador.getId(),
                        donaciones
                );
            } catch (Exception e) {
                System.err.println(
                        "Error procesando donador "
                                + donador.getId()
                                + ": "
                                + e.getMessage()
                );
            }
        }
    }
}







