package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "donaciones",
        url = "${donaciones.url}"
)
public interface CategoriasClient {

    @GetMapping("/categorias/search/{productoID}")
    CategoriaDTO buscarCategoriaPorProductoID(
            @PathVariable("productoID") String productoID
    );
}