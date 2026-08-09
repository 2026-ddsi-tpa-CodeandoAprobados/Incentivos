package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IncentivosEntrega4Test {

    private DonadorRepository donadorRepository;
    private MisionRepository misionRepository;
    private InsigniaRepository insigniaRepository;

    private CategoriasClient categoriasClient;
    private DonadoresClient donadoresClient;
    private IncentivosMetrics metrics;

    private IncentivosService service;

    private DonadorIncentivos donador;
    private Mision mision;
    private Insignia insignia;

    @BeforeEach
    void setUp() {

        donadorRepository = mock(DonadorRepository.class);
        misionRepository = mock(MisionRepository.class);
        insigniaRepository = mock(InsigniaRepository.class);

        categoriasClient = mock(CategoriasClient.class);
        donadoresClient = mock(DonadoresClient.class);
        metrics = mock(IncentivosMetrics.class);

        service = new IncentivosService(
                donadorRepository,
                misionRepository,
                insigniaRepository,
                categoriasClient,
                donadoresClient,
                metrics
        );

        insignia = new Insignia(
                "insignia-1",
                "Donador constante",
                "Completó 20 donaciones exitosas"
        );

        mision = new Mision(
                "mision-1",
                "Donaciones exitosas",
                insignia.getId(),
                CategoriaDonadorEnum.COLABORADOR,
                CategoriaDonadorEnum.TRANSFORMADOR,
                TipoMisionEnum.DONACIONES_EXITOSAS
        );

        donador = new DonadorIncentivos("donador-1");

        when(donadorRepository.findById("donador-1"))
                .thenReturn(Optional.of(donador));

        when(insigniaRepository.findById("insignia-1"))
                .thenReturn(Optional.of(insignia));
    }

    @Test
    void con19DonacionesAceptadasNoCompletaLaMision() {

        donador.setMisionEnCurso(mision);

        List<DonacionDTO> donaciones =
                crearDonacionesAceptadas(19);

        service.procesarDonador(
                "donador-1",
                donaciones
        );

        assertEquals(
                mision,
                donador.getMisionEnCurso()
        );

        assertTrue(
                donador.getInsignias().isEmpty()
        );

        assertTrue(
                donador.getMisionesCompletadas().isEmpty()
        );

        verify(
                donadoresClient,
                never()
        ).actualizarCategoria(
                any(),
                any()
        );
    }

    @Test
    void con20DonacionesAceptadasCompletaLaMision() {

        donador.setMisionEnCurso(mision);

        List<DonacionDTO> donaciones =
                crearDonacionesAceptadas(20);

        service.procesarDonador(
                "donador-1",
                donaciones
        );

        // La misión deja de estar en curso.
        assertNull(
                donador.getMisionEnCurso()
        );

        // Recibió la insignia.
        assertTrue(
                donador.getInsignias()
                        .contains(insignia)
        );

        // Registramos la misión como completada.
        assertTrue(
                donador.getMisionesCompletadas()
                        .contains(mision)
        );

        // Se actualizó la categoría.
        verify(
                donadoresClient,
                times(1)
        ).actualizarCategoria(
                eq("donador-1"),
                any()
        );

        // Se persistió el cambio.
        verify(
                donadorRepository,
                times(1)
        ).save(donador);
    }

    @Test
    void siBajaDe20AceptadasPierdeElProgreso() {

        /*
         * Simulamos que el donador anteriormente
         * ya había completado la misión.
         */

        donador.agregarInsignia(insignia);
        donador.completarMision(mision);

        assertTrue(
                donador.getInsignias()
                        .contains(insignia)
        );

        assertTrue(
                donador.getMisionesCompletadas()
                        .contains(mision)
        );

        /*
         * Ahora solamente tiene 19 donaciones ACEPTADAS.
         *
         * Esto representa el caso donde alguna donación
         * recibió una queja y dejó de contar como ACEPTADA.
         */

        List<DonacionDTO> donaciones =
                crearDonacionesAceptadas(19);

        service.procesarDonador(
                "donador-1",
                donaciones
        );

        // Pierde la insignia.
        assertFalse(
                donador.getInsignias()
                        .contains(insignia)
        );

        // La misión deja de estar entre las completadas.
        assertFalse(
                donador.getMisionesCompletadas()
                        .contains(mision)
        );

        // La misión vuelve a quedar en curso.
        assertEquals(
                mision,
                donador.getMisionEnCurso()
        );

        // Se actualiza nuevamente la categoría,
        // esta vez hacia COLABORADOR.
        verify(
                donadoresClient,
                times(1)
        ).actualizarCategoria(
                eq("donador-1"),
                any()
        );

        // Persistimos el nuevo estado.
        verify(
                donadorRepository,
                times(1)
        ).save(donador);
    }

    private List<DonacionDTO> crearDonacionesAceptadas(
            int cantidad
    ) {

        List<DonacionDTO> donaciones =
                new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {

            DonacionDTO donacion =
                    new DonacionDTO(
                            "donacion-" + i,
                            "donador-1",
                            "deposito-1",
                            "Donacion de prueba",
                            List.of(),
                            EstadoDonacionEnum.ACEPTADA,
                            LocalDate.now()
                    );

            donaciones.add(donacion);
        }

        return donaciones;
    }
}