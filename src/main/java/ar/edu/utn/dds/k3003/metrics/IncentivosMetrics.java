package ar.edu.utn.dds.k3003.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class IncentivosMetrics {

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

        this.errores = registry.counter("incentivos.errores");
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
    public void registrarInsigniaCreada() {
        insigniasCreadas.increment();
    }

    public void registrarMisionCreada() {
        misionesCreadas.increment();
    }

    public void registrarConsultaDonador() {
        consultasDonador.increment();
    }

    public void registrarConsultaInsignia() {
        consultasInsignia.increment();
    }

    public void registrarConsultaMision() {
        consultasMision.increment();
    }
    public void registrarError() {
        errores.increment();
    }

}