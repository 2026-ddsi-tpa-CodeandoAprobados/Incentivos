package ar.edu.utn.dds.k3003.dtos;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;

import java.util.List;

public record DonadorIncentivosDetalleDTO(
        String id,
        MisionDTO misionEnCurso,
        List<MisionDTO> misionesCompletadas,
        List<InsigniaDTO> insignias
) {
}