package ar.edu.utn.dds.k3003;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IncentivosCoberturaTest {

  private Fachada fachada;

  private FachadaDonadoresYEntidades fachadaDonadores;

  @BeforeEach
  void setUp() {

    fachada = new Fachada();

    fachadaDonadores =
            mock(FachadaDonadoresYEntidades.class);

    fachada.setFachadaDonadoresYEntidades(
            fachadaDonadores
    );

    fachada.setFachadaDonaciones(
            mock(FachadaDonaciones.class)
    );
  }

  @Test
  void testProcesarDonadorCobertura() {

    when(
            fachadaDonadores.buscarDonadorPorID(
                    "donador123"
            )
    ).thenReturn(
            new DonadorDTO(
                    "donador123",
                    "nombre",
                    "apellido",
                    20,
                    "direccion",
                    "mail",
                    "telefono",
                    EstadoDonadorEnum.VERIFICADO,
                    "zona"
            )
    );

    MisionDTO mision =
            fachada.agregarMision(
                    new MisionDTO(
                            null,
                            "mision",
                            "insignia",
                            null,
                            null,
                            TipoMisionEnum.COMPLETITUD
                    )
            );

    fachada.asignarMisionADonador(
            "donador123",
            mision
    );

    assertDoesNotThrow(
            () -> fachada.procesarDonador(
                    "donador123"
            )
    );

    assertThrows(
            RuntimeException.class,
            () -> fachada.procesarDonador(null)
    );
  }

  @Test
  void testAgregarInsigniaYidNotNull() {

    InsigniaDTO conId =
            new InsigniaDTO(
                    "ya-tengo-id",
                    "nombre",
                    "desc"
            );

    assertThrows(
            RuntimeException.class,
            () -> fachada.agregarInsignia(conId)
    );
  }

  @Test
  void testAgregarMisionYidNotNull() {

    MisionDTO conId =
            new MisionDTO(
                    "id-inválido",
                    "mision",
                    "insignia",
                    null,
                    null,
                    TipoMisionEnum.COMPLETITUD
            );

    assertThrows(
            RuntimeException.class,
            () -> fachada.agregarMision(conId)
    );
  }

  @Test
  void testGetInsigniasDeDonadorSinInsignias() {

    when(
            fachadaDonadores.buscarDonadorPorID(
                    "donadorSinInsignias"
            )
    ).thenReturn(
            new DonadorDTO(
                    "donadorSinInsignias",
                    "nombre",
                    "apellido",
                    20,
                    "direccion",
                    "mail",
                    "telefono",
                    EstadoDonadorEnum.VERIFICADO,
                    "zona"
            )
    );

    MisionDTO mision =
            fachada.agregarMision(
                    new MisionDTO(
                            null,
                            "mision",
                            "insignia",
                            null,
                            null,
                            TipoMisionEnum.COMPLETITUD
                    )
            );

    fachada.asignarMisionADonador(
            "donadorSinInsignias",
            mision
    );

    var insignias =
            fachada.getInsigniasDeDonador(
                    "donadorSinInsignias"
            );

    assertNotNull(insignias);

    assertTrue(insignias.isEmpty());
  }

  @Test
  void testGetMisionEnCursoDeDonadorSinMision() {

    when(
            fachadaDonadores.buscarDonadorPorID(
                    "donadorSinMision"
            )
    ).thenReturn(
            new DonadorDTO(
                    "donadorSinMision",
                    "nombre",
                    "apellido",
                    20,
                    "direccion",
                    "mail",
                    "telefono",
                    EstadoDonadorEnum.VERIFICADO,
                    "zona"
            )
    );

    fachada.asignarInsigniaADonador(
            "donadorSinMision",
            new InsigniaDTO(
                    "id",
                    "nombre",
                    "desc"
            )
    );

    MisionDTO mision =
            fachada.getMisionEnCursoDeDonador(
                    "donadorSinMision"
            );

    assertNull(mision);
  }
}