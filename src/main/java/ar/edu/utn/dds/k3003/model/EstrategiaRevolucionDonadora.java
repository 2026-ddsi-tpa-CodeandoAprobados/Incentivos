package ar.edu.utn.dds.k3003.model;

import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;

public class EstrategiaRevolucionDonadora implements EstrategiaMision {

    @Override
    public boolean estaCumplida(
            List<DonacionDTO> donaciones,
            CategoriasClient categoriasClient
    ) {

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