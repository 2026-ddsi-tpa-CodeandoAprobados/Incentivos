package ar.edu.utn.dds.k3003.model;

import java.util.Comparator;
import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;

public class EstrategiaDonacionesAscendentes implements EstrategiaMision {

    @Override
    public boolean estaCumplida(
            List<DonacionDTO> donaciones,
            CategoriasClient categoriasClient
    ) {

        if (donaciones.size() < 5) {
            return false;
        }

        List<DonacionDTO> ordenadas = donaciones.stream()
                .sorted(Comparator.comparing(DonacionDTO::fechaRegistro))
                .toList();

        List<DonacionDTO> ultimas =
                ordenadas.subList(ordenadas.size() - 5, ordenadas.size());

        for (int i = 0; i < ultimas.size() - 1; i++) {

            int actual = ultimas.get(i)
                    .detallesProductosDTO()
                    .stream()
                    .mapToInt(det -> det.cantidadProducto())
                    .sum();

            int siguiente = ultimas.get(i + 1)
                    .detallesProductosDTO()
                    .stream()
                    .mapToInt(det -> det.cantidadProducto())
                    .sum();

            if (actual >= siguiente) {
                return false;
            }
        }

        return true;
    }
}