package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;
import ar.edu.utn.dds.k3003.clients.DonadoresClient;
import ar.edu.utn.dds.k3003.metrics.IncentivosMetrics;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.TipoMisionEnum;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.repositories.InsigniaRepository;
import ar.edu.utn.dds.k3003.repositories.MisionRepository;
import ar.edu.utn.dds.k3003.services.IncentivosService;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IdsDeDonadoresConMisionesTest {

    @Test
    void devuelveSoloDonadoresConMisionEnCursoOCompletadas() {
        DonadorRepository donadorRepository = mock(DonadorRepository.class);
        IncentivosService service = new IncentivosService(
                donadorRepository,
                mock(MisionRepository.class),
                mock(InsigniaRepository.class),
                mock(CategoriasClient.class),
                mock(DonadoresClient.class),
                mock(IncentivosMetrics.class)
        );

        Mision mision = new Mision(
                "m1", "Donaciones exitosas", "i1",
                CategoriaDonadorEnum.COLABORADOR,
                CategoriaDonadorEnum.TRANSFORMADOR,
                TipoMisionEnum.DONACIONES_EXITOSAS
        );

        DonadorIncentivos conMisionEnCurso = new DonadorIncentivos("a");
        conMisionEnCurso.setMisionEnCurso(mision);

        DonadorIncentivos conMisionCompletada = new DonadorIncentivos("b");
        conMisionCompletada.completarMision(mision);

        DonadorIncentivos sinNada = new DonadorIncentivos("c");

        when(donadorRepository.findAll())
                .thenReturn(List.of(conMisionEnCurso, conMisionCompletada, sinNada));

        assertEquals(List.of("a", "b"), service.idsDeDonadoresConMisiones());
    }
}
