package ar.edu.utn.dds.k3003.scheduler;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.DonacionesClient;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.services.IncentivosService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcesadorMisionesScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(ProcesadorMisionesScheduler.class);

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

    @Scheduled(fixedDelayString = "#{@schedulerConfig.intervalo}")
    public void procesarMisiones() {
        log.info("CRON INCENTIVOS - Iniciando procesamiento periódico");
        List<DonadorIncentivos> donadores =
                donadorRepository.findAll();
        log.info(
                "CRON INCENTIVOS - Donadores encontrados en Incentivos: {}",
                donadores.size()
        );
        int procesados = 0;
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
                log.info(
                        "CRON INCENTIVOS - Procesando donador {}",
                        donador.getId()
                );
                List<DonacionDTO> donaciones =
                        donacionesClient.buscarPorDonador(
                                donador.getId()
                        );
                incentivosService.procesarDonador(
                        donador.getId(),
                        donaciones
                );
                procesados++;
            } catch (Exception e) {
                log.error(
                        "CRON INCENTIVOS - Error procesando donador {}: {}",
                        donador.getId(),
                        e.getMessage()
                );
            }
        }
        log.info(
                "CRON INCENTIVOS - Procesamiento terminado. Donadores procesados: {}",
                procesados
        );
    }
}