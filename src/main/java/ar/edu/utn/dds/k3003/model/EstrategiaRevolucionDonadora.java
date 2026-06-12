package ar.edu.utn.dds.k3003.model;

import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;

public class EstrategiaRevolucionDonadora implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<DonacionDTO> donaciones) {

        long grandes = donaciones.stream()
                .filter(d ->
                        d.detallesProductosDTO()
                                .stream()
                                .mapToInt(det -> det.cantidadProducto())
                                .sum() > 50
                )
                .count();

        return grandes > 10;
    }
}