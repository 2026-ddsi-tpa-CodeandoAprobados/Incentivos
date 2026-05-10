package ar.edu.utn.dds.k3003.model;

import java.util.List;

public class EstrategiaRevolucionDonadora implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<Donacion> donaciones) {

        long grandesDonaciones =
                donaciones.stream()
                        .filter(d -> d.getCantidad() > 50)
                        .count();

        return grandesDonaciones > 10;
    }
}