package ar.edu.utn.dds.k3003.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class RequestLoggingFilterTest {

  private final RequestLoggingFilter filtro = new RequestLoggingFilter("8080");

  @AfterEach
  void limpiar() {
    MDC.clear();
  }

  @Test
  void propagaLaTrazaRecibidaEnLaRespuesta() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/misiones");
    request.addHeader(Traza.ENCABEZADO, "prueba-1");
    MockHttpServletResponse response = new MockHttpServletResponse();

    filtro.doFilter(request, response, new MockFilterChain());

    assertEquals("prueba-1", response.getHeader(Traza.ENCABEZADO));
  }

  @Test
  void generaUnaTrazaSiElPedidoNoTraeNinguna() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/misiones");
    MockHttpServletResponse response = new MockHttpServletResponse();

    filtro.doFilter(request, response, new MockFilterChain());

    assertNotNull(response.getHeader(Traza.ENCABEZADO));
  }

  @Test
  void noRegistraLosPedidosDeActuator() {
    assertTrue(filtro.shouldNotFilter(new MockHttpServletRequest("GET", "/actuator/health")));
    assertFalse(filtro.shouldNotFilter(new MockHttpServletRequest("GET", "/misiones")));
  }

  @Test
  void elInterceptorReenviaLaTrazaDelHilo() {
    MDC.put(Traza.MDC_TRAZA, "t-42");
    RequestTemplate plantilla = new RequestTemplate();

    new TrazaFeignInterceptor().apply(plantilla);

    assertTrue(plantilla.headers().get(Traza.ENCABEZADO).contains("t-42"));
  }

  @Test
  void elInterceptorNoAgregaNadaSinTraza() {
    RequestTemplate plantilla = new RequestTemplate();

    new TrazaFeignInterceptor().apply(plantilla);

    assertFalse(plantilla.headers().containsKey(Traza.ENCABEZADO));
  }
}
