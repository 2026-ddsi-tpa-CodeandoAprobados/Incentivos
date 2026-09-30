package ar.edu.utn.dds.k3003.model;

import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;

public class EstrategiaDonacionesExitosas implements EstrategiaMision {

    public static final int CANTIDAD_POR_DEFECTO = 20;

    private final int cantidadRequerida;

    public EstrategiaDonacionesExitosas() {
        this(CANTIDAD_POR_DEFECTO);
    }

    public EstrategiaDonacionesExitosas(int cantidadRequerida) {
        if (cantidadRequerida < 1) {
            throw new IllegalArgumentException(
                    "La cantidad requerida de donaciones debe ser al menos 1"
            );
        }
        this.cantidadRequerida = cantidadRequerida;
    }

    @Override
    public boolean estaCumplida(
            List<DonacionDTO> donaciones,
            CategoriasClient categoriasClient
    ) {

        long exitosas = donaciones.stream()
                .filter(d -> d.estado() == EstadoDonacionEnum.ACEPTADA)
                .count();

        return exitosas >= cantidadRequerida;
    }
}