package ar.edu.utn.dds.k3003.model;

import java.util.List;

public class ProcesadorMisiones {

    public boolean procesar(Mision mision, List<Donacion> donaciones) {

        EstrategiaMision estrategia =
                obtenerEstrategia(mision.getTipo());

        return estrategia.estaCumplida(donaciones);
    }

    private EstrategiaMision obtenerEstrategia(TipoMisionEnum tipo) {

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