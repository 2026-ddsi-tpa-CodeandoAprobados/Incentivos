package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;

import java.util.List;

public class EstrategiaCompletitud implements EstrategiaMision {

    @Override
    public boolean estaCumplida(List<DonacionDTO> donaciones) {

        // TODO: recorrer los productos de cada donación y consultar
        // al módulo Donaciones la categoría de cada producto.
        // La misión se cumple cuando existan al menos 3 categorías distintas.

        throw new UnsupportedOperationException("Pendiente implementar");
    }
}