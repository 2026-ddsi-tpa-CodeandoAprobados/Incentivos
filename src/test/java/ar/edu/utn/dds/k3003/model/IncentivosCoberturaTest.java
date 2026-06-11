/*

package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.TipoMisionEnum;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.repositories.InsigniaRepository;
import ar.edu.utn.dds.k3003.repositories.MisionRepository;
import ar.edu.utn.dds.k3003.services.IncentivosService;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IncentivosCoberturaTest {

    private DonadorRepository donadorRepository;
    private MisionRepository misionRepository;
    private InsigniaRepository insigniaRepository;

    private IncentivosService service;

    @BeforeEach
    void init() {
        donadorRepository = mock(DonadorRepository.class);
        misionRepository = mock(MisionRepository.class);
        insigniaRepository = mock(InsigniaRepository.class);

        service = new IncentivosService(
                donadorRepository,
                misionRepository,
                insigniaRepository
        );
    }

    @Test
    void guardarYBuscarInsignia() {

        Insignia insignia =
                new Insignia("1", "Gold", "Descripcion");

        when(insigniaRepository.findById("1"))
                .thenReturn(Optional.of(insignia));

        service.guardarInsignia(insignia);

        Insignia buscada =
                service.buscarInsignia("1");

        assertNotNull(buscada);
        assertEquals("Gold", buscada.getNombre());

        verify(insigniaRepository).save(insignia);
    }

    @Test
    void buscarInsigniaInexistente() {

        when(insigniaRepository.findById("999"))
                .thenReturn(Optional.empty());

        assertNull(
                service.buscarInsignia("999")
        );
    }

    @Test
    void guardarYBuscarMision() {

        Mision mision =
                new Mision(
                        "1",
                        "Mi misión",
                        "ins1",
                        CategoriaDonadorEnum.OCASIONAL,
                        CategoriaDonadorEnum.COLABORADOR,
                        TipoMisionEnum.COMPLETITUD
                );

        when(misionRepository.findById("1"))
                .thenReturn(Optional.of(mision));

        service.guardarMision(mision);

        assertEquals(
                "Mi misión",
                service.buscarMision("1").getNombre()
        );

        verify(misionRepository).save(mision);
    }

    @Test
    void obtenerOCrearDonadorExistente() {

        DonadorIncentivos donador =
                new DonadorIncentivos("abc");

        when(donadorRepository.findById("abc"))
                .thenReturn(Optional.of(donador));

        DonadorIncentivos resultado =
                service.obtenerOCrearDonador("abc");

        assertEquals("abc", resultado.getId());

        verify(donadorRepository, never())
                .save(any());
    }

    @Test
    void obtenerOCrearDonadorNuevo() {

        when(donadorRepository.findById("nuevo"))
                .thenReturn(Optional.empty());

        DonadorIncentivos resultado =
                service.obtenerOCrearDonador("nuevo");

        assertEquals("nuevo", resultado.getId());

        verify(donadorRepository)
                .save(any(DonadorIncentivos.class));
    }

    @Test
    void obtenerDonadorInexistenteLanzaExcepcion() {

        when(donadorRepository.findById("x"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> service.obtenerDonador("x")
        );
    }

    @Test
    void buscarTodasLasInsignias() {

        when(insigniaRepository.findAll())
                .thenReturn(
                        List.of(
                                new Insignia(
                                        "1",
                                        "A",
                                        "B"
                                )
                        )
                );

        assertEquals(
                1,
                service.buscarTodasLasInsignias().size()
        );
    }

    @Test
    void buscarTodasLasMisiones() {

        when(misionRepository.findAll())
                .thenReturn(List.of());

        assertTrue(
                service.buscarTodasLasMisiones().isEmpty()
        );
    }
}

*/