package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "donadores-client",
        url = "${donadores.url}"
)
public interface DonadoresClient {

    @GetMapping("/donadores/{donadorID}")
    DonadorDTO buscarDonadorPorID(
            @PathVariable String donadorID
    );
}