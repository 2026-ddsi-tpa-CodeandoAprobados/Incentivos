package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DetalleProductoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EstrategiaCompletitud implements EstrategiaMision {

    @Override
    public boolean estaCumplida(
            List<DonacionDTO> donaciones,
            CategoriasClient categoriasClient
    ) {

        Set<String> categorias = new HashSet<>();

        for (DonacionDTO donacion : donaciones) {
            for (DetalleProductoDTO detalle : donacion.detallesProductosDTO()) {

                CategoriaDTO categoria =
                        categoriasClient.buscarCategoriaPorProductoID(
                                detalle.productoID()
                        );

                categorias.add(categoria.id());
            }
        }

        return categorias.size() >= 3;
    }
}