package ar.edu.utn.dds.k3003.model;

import java.util.List;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;

public interface EstrategiaMision {

    boolean estaCumplida(
            List<DonacionDTO> donaciones,
            CategoriasClient categoriasClient
    );

}