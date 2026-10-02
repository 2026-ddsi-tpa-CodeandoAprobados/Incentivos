package ar.edu.utn.dds.k3003.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

public class TrazaTest {

  @AfterEach
  void limpiar() {
    MDC.clear();
  }

  @Test
  void conservaUnaTrazaValida() {
    assertEquals("prueba-1", Traza.recibirOGenerar("prueba-1"));
  }

  @Test
  void generaUnaNuevaSiNoHayTraza() {
    assertEquals(8, Traza.recibirOGenerar(null).length());
  }

  @Test
  void rechazaUnaTrazaConSaltosDeLinea() {
    String maliciosa = "abc\nFALSA linea de log";
    assertNotEquals(maliciosa, Traza.recibirOGenerar(maliciosa));
  }

  @Test
  void actualDevuelveLaTrazaDelHilo() {
    assertNull(Traza.actual());
    MDC.put(Traza.MDC_TRAZA, "t-1");
    assertEquals("t-1", Traza.actual());
  }
}
