package ar.edu.utn.dds.k3003.clients;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = "donaciones",
        url = "${donaciones.url}"
)
public interface DonacionesClient {

    @GetMapping("/donaciones/search")
    List<DonacionDTO> buscarPorDonadorYFechaInicio(
            @RequestParam("donadorID") String donadorID,
            @RequestParam("fechaInicio") LocalDate fechaInicio
    );

    @GetMapping("/donaciones/search/{donadorID}")
    List<DonacionDTO> buscarPorDonador(
            @PathVariable("donadorID") String donadorID
    );

}

