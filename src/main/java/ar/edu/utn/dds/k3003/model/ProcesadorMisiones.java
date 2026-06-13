package ar.edu.utn.dds.k3003.model;

import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;

public class ProcesadorMisiones {

    public boolean procesar(
            Mision mision,
            List<DonacionDTO> donaciones,
            CategoriasClient categoriasClient
    ) {

        EstrategiaMision estrategia =
                obtenerEstrategia(mision.getTipo());

        return estrategia.estaCumplida(
                donaciones,
                categoriasClient
        );
    }

    private EstrategiaMision obtenerEstrategia(
            TipoMisionEnum tipo
    ) {

        return switch (tipo) {

            case COMPLETITUD ->
                    new EstrategiaCompletitud();

            case DONACIONES_EXITOSAS ->
                    new EstrategiaDonacionesExitosas();

            case DONACIONES_ASCENDENTES ->
                    new EstrategiaDonacionesAscendentes();

            case REVOLUCION_DONADORA ->
                    new EstrategiaRevolucionDonadora();
        };
    }
}