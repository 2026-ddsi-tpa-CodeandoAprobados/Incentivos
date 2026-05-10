package ar.edu.utn.dds.k3003;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IncentivosCoberturaTest {

  private Fachada fachada;

  @BeforeEach
  void setUp() {

    fachada = new Fachada();

    fachada.setFachadaDonadoresYEntidades(
            mock(FachadaDonadoresYEntidades.class)
    );

    fachada.setFachadaDonaciones(
            mock(FachadaDonaciones.class)
    );
  }

  @Test
  void testProcesarDonadorCobertura() {

    assertDoesNotThrow(
            () -> fachada.procesarDonador("donador123")
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

    fachada.procesarDonador(
            "donadorSinInsignias"
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

    fachada.procesarDonador(
            "donadorSinMision"
    );

    MisionDTO mision =
            fachada.getMisionEnCursoDeDonador(
                    "donadorSinMision"
            );

    assertNull(mision);
  }
}