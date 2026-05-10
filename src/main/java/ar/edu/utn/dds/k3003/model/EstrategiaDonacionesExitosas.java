package ar.edu.utn.dds.k3003.model;

import java.util.List;

public class EstrategiaDonacionesExitosas implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<Donacion> donaciones) {

        long exitosas =
                donaciones.stream()
                        .filter(Donacion::isAceptada)
                        .filter(d -> !d.isConQueja())
                        .count();

        return exitosas >= 20;
    }
}