package ar.edu.utn.dds.k3003.scheduler;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.DonacionesClient;
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

    private final DonacionesClient donacionesClient;
    private final IncentivosService incentivosService;

    public ProcesadorMisionesScheduler(
            DonacionesClient donacionesClient,
            IncentivosService incentivosService
    ) {
        this.donacionesClient = donacionesClient;
        this.incentivosService = incentivosService;
    }

    @Scheduled(fixedDelayString = "#{@schedulerConfig.intervalo}")
    public void procesarMisiones() {
        log.info("CRON INCENTIVOS - Iniciando procesamiento periódico");
        List<String> donadorIds =
                incentivosService.idsDeDonadoresConMisiones();
        log.info(
                "CRON INCENTIVOS - Donadores con misiones a procesar: {}",
                donadorIds.size()
        );
        int procesados = 0;
        for (String donadorId : donadorIds) {
            try {
                log.info(
                        "CRON INCENTIVOS - Procesando donador {}",
                        donadorId
                );
                List<DonacionDTO> donaciones =
                        donacionesClient.buscarPorDonador(donadorId);
                incentivosService.procesarDonador(
                        donadorId,
                        donaciones
                );
                procesados++;
            } catch (Exception e) {
                log.error(
                        "CRON INCENTIVOS - Error procesando donador {}: {}",
                        donadorId,
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