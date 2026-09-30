package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;
import ar.edu.utn.dds.k3003.clients.DonadoresClient;
import ar.edu.utn.dds.k3003.metrics.IncentivosMetrics;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.EstrategiaDonacionesExitosas;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.TipoMisionEnum;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.repositories.InsigniaRepository;
import ar.edu.utn.dds.k3003.repositories.MisionRepository;
import ar.edu.utn.dds.k3003.services.IncentivosService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MisionDonacionesExitosasConfigurableTest {

    private DonadorRepository donadorRepository;
    private MisionRepository misionRepository;
    private InsigniaRepository insigniaRepository;
    private DonadoresClient donadoresClient;
    private IncentivosService service;

    private DonadorIncentivos donador;
    private Mision mision;
    private Insignia insignia;

    @BeforeEach
    void setUp() {
        donadorRepository = mock(DonadorRepository.class);
        misionRepository = mock(MisionRepository.class);
        insigniaRepository = mock(InsigniaRepository.class);
        donadoresClient = mock(DonadoresClient.class);

        service = new IncentivosService(
                donadorRepository,
                misionRepository,
                insigniaRepository,
                mock(CategoriasClient.class),
                donadoresClient,
                mock(IncentivosMetrics.class)
        );

        insignia = new Insignia("insignia-1", "Donador constante", "Donaciones exitosas");

        mision = new Mision(
                "mision-1",
                "Donaciones exitosas",
                insignia.getId(),
                CategoriaDonadorEnum.COLABORADOR,
                CategoriaDonadorEnum.TRANSFORMADOR,
                TipoMisionEnum.DONACIONES_EXITOSAS
        );

        donador = new DonadorIncentivos("donador-1");

        when(donadorRepository.findById("donador-1")).thenReturn(Optional.of(donador));
        when(insigniaRepository.findById("insignia-1")).thenReturn(Optional.of(insignia));
    }

    @Test
    void sinConfigurarLaCantidadPorDefectoEs20() {
        assertNull(mision.getCantidadRequerida());
        assertEquals(20, mision.cantidadRequeridaEfectiva());
    }

    @Test
    void sinConfigurarSigueNecesitando20() {
        donador.setMisionEnCurso(mision);

        service.procesarDonador("donador-1", aceptadas(19));
        assertEquals(mision, donador.getMisionEnCurso());

        service.procesarDonador("donador-1", aceptadas(20));
        assertNull(donador.getMisionEnCurso());
        assertTrue(donador.getMisionesCompletadas().contains(mision));
    }

    @Test
    void conCantidadConfiguradaEnTresConDosNoCompleta() {
        mision.setCantidadRequerida(3);
        donador.setMisionEnCurso(mision);

        service.procesarDonador("donador-1", aceptadas(2));

        assertEquals(mision, donador.getMisionEnCurso());
        assertTrue(donador.getInsignias().isEmpty());
        verify(donadoresClient, never()).actualizarCategoria(any(), any());
    }

    @Test
    void conCantidadConfiguradaEnTresConTresCompleta() {
        mision.setCantidadRequerida(3);
        donador.setMisionEnCurso(mision);

        service.procesarDonador("donador-1", aceptadas(3));

        assertNull(donador.getMisionEnCurso());
        assertTrue(donador.getInsignias().contains(insignia));
        assertTrue(donador.getMisionesCompletadas().contains(mision));
        verify(donadoresClient, times(1)).actualizarCategoria(eq("donador-1"), any());
    }

    @Test
    void conCantidadConfiguradaPierdeElProgresoAlBajarDeN() {
        mision.setCantidadRequerida(3);
        donador.agregarInsignia(insignia);
        donador.completarMision(mision);

        service.procesarDonador("donador-1", aceptadas(2));

        assertFalse(donador.getInsignias().contains(insignia));
        assertFalse(donador.getMisionesCompletadas().contains(mision));
        assertEquals(mision, donador.getMisionEnCurso());
        verify(donadoresClient, times(1)).actualizarCategoria(eq("donador-1"), any());
    }

    @Test
    void configurarCantidadRequeridaPersisteElValor() {
        when(misionRepository.findById("mision-1")).thenReturn(Optional.of(mision));
        when(misionRepository.save(mision)).thenReturn(mision);

        Mision resultado = service.configurarCantidadRequerida("mision-1", 5);

        assertEquals(5, resultado.cantidadRequeridaEfectiva());
        verify(misionRepository, times(1)).save(mision);
    }

    @Test
    void cantidadInvalidaSeRechaza() {
        assertThrows(IllegalArgumentException.class, () -> mision.setCantidadRequerida(0));
        assertThrows(IllegalArgumentException.class, () -> mision.setCantidadRequerida(-1));
        assertThrows(IllegalArgumentException.class, () -> new EstrategiaDonacionesExitosas(0));
    }

    private List<DonacionDTO> aceptadas(int cantidad) {
        List<DonacionDTO> donaciones = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            donaciones.add(new DonacionDTO(
                    "donacion-" + i,
                    "donador-1",
                    "deposito-1",
                    "Donacion de prueba",
                    List.of(),
                    EstadoDonacionEnum.ACEPTADA,
                    LocalDate.now()
            ));
        }
        return donaciones;
    }
}
