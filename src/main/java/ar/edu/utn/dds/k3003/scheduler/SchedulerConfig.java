package ar.edu.utn.dds.k3003.scheduler;

import org.springframework.stereotype.Component;

@Component
public class SchedulerConfig {

    private long intervalo = 60000;

    public long getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(long intervalo) {
        this.intervalo = intervalo;
    }
}