package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "donadores-client",
        url = "${donadores.url}"
)
public interface DonadoresClient {

    @GetMapping("/donadores/{donadorID}")
    DonadorDTO buscarDonadorPorID(
            @PathVariable String donadorID
    );

    @PatchMapping("/donadores/{id}/categoria")
    void actualizarCategoria(
            @PathVariable("id") String donadorID,
            @RequestBody CategoriaRequest request
    );
}