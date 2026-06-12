package ar.edu.utn.dds.k3003.model;

import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;

public class EstrategiaDonacionesExitosas implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<DonacionDTO> donaciones) {

        long exitosas = donaciones.stream()
                .filter(d -> d.estado() == EstadoDonacionEnum.ACEPTADA)
                .count();

        return exitosas >= 20;
    }
}