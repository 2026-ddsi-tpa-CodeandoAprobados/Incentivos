package ar.edu.utn.dds.k3003.model;

import java.util.List;

public class EstrategiaDonacionesAscendentes implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<Donacion> donaciones) {

        if (donaciones.size() < 5) {
            return false;
        }

        List<Donacion> ultimas =
                donaciones.subList(donaciones.size() - 5, donaciones.size());

        for (int i = 0; i < ultimas.size() - 1; i++) {

            if (ultimas.get(i).getCantidad()
                    >= ultimas.get(i + 1).getCantidad()) {

                return false;
            }
        }

        return true;
    }
}