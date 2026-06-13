package ar.edu.utn.dds.k3003.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class IncentivosMetrics {

    private final Counter donadoresProcesados;
    private final Counter misionesCumplidas;
    private final Counter insigniasAsignadas;

    public IncentivosMetrics(MeterRegistry registry) {
        this.donadoresProcesados =
                registry.counter("incentivos.donadores.procesados");

        this.misionesCumplidas =
                registry.counter("incentivos.misiones.cumplidas");

        this.insigniasAsignadas =
                registry.counter("incentivos.insignias.asignadas");
    }

    public void registrarProcesamiento() {
        donadoresProcesados.increment();
    }

    public void registrarMisionCumplida() {
        misionesCumplidas.increment();
    }

    public void registrarInsigniaAsignada() {
        insigniasAsignadas.increment();
    }
}