package ar.edu.utn.dds.k3003.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IncentivosMetrics {

    private static final Logger log =
            LoggerFactory.getLogger(IncentivosMetrics.class);

    private final Counter donadoresProcesados;
    private final Counter misionesCumplidas;
    private final Counter insigniasAsignadas;
    private final Counter insigniasCreadas;
    private final Counter misionesCreadas;
    private final Counter consultasDonador;
    private final Counter consultasInsignia;
    private final Counter consultasMision;
    private final Counter errores;

    public IncentivosMetrics(MeterRegistry registry) {

        this.donadoresProcesados =
                registry.counter("incentivos.donadores.procesados");

        this.misionesCumplidas =
                registry.counter("incentivos.misiones.cumplidas");

        this.insigniasAsignadas =
                registry.counter("incentivos.insignias.asignadas");

        this.insigniasCreadas =
                registry.counter("incentivos.insignias.creadas");

        this.misionesCreadas =
                registry.counter("incentivos.misiones.creadas");

        this.consultasDonador =
                registry.counter("incentivos.consultas.donador");

        this.consultasInsignia =
                registry.counter("incentivos.consultas.insignia");

        this.consultasMision =
                registry.counter("incentivos.consultas.mision");

        this.errores =
                registry.counter("incentivos.errores");
    }

    public void registrarProcesamiento() {
        donadoresProcesados.increment();
        log.info("METRICA INCENTIVOS - Donador procesado");
    }

    public void registrarMisionCumplida() {
        misionesCumplidas.increment();
        log.info("METRICA INCENTIVOS - Mision cumplida");
    }

    public void registrarInsigniaAsignada() {
        insigniasAsignadas.increment();
        log.info("METRICA INCENTIVOS - Insignia asignada");
    }

    public void registrarInsigniaCreada() {
        insigniasCreadas.increment();
        log.info("METRICA INCENTIVOS - Insignia creada");
    }

    public void registrarMisionCreada() {
        misionesCreadas.increment();
        log.info("METRICA INCENTIVOS - Mision creada");
    }

    public void registrarConsultaDonador() {
        consultasDonador.increment();
        log.info("METRICA INCENTIVOS - Consulta de donador");
    }

    public void registrarConsultaInsignia() {
        consultasInsignia.increment();
        log.info("METRICA INCENTIVOS - Consulta de insignia");
    }

    public void registrarConsultaMision() {
        consultasMision.increment();
        log.info("METRICA INCENTIVOS - Consulta de mision");
    }

    public void registrarError() {
        errores.increment();
        log.error("METRICA INCENTIVOS - Error registrado");
    }
}