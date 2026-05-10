package ar.edu.utn.dds.k3003.model;

import java.util.List;
import java.util.stream.Collectors;

public class EstrategiaCompletitud implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<Donacion> donaciones) {

        long categoriasDistintas =
                donaciones.stream()
                        .map(Donacion::getCategoria)
                        .distinct()
                        .count();

        return categoriasDistintas >= 3;
    }
}