package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Mision {

  @Id
  private String id;

  private String nombre;

  private String insigniaID;

  @Enumerated(EnumType.STRING)
  private CategoriaDonadorEnum categoriaInicio;

  @Enumerated(EnumType.STRING)
  private CategoriaDonadorEnum categoriaFin;

  @Enumerated(EnumType.STRING)
  private TipoMisionEnum tipo;

  // Solo aplica a DONACIONES_EXITOSAS. Si es null se usa el valor por defecto (20).
  private Integer cantidadRequerida;

  public Mision() {
  }

  public Mision(
          String id,
          String nombre,
          String insigniaID,
          CategoriaDonadorEnum categoriaInicio,
          CategoriaDonadorEnum categoriaFin,
          TipoMisionEnum tipo
  ) {
    this.id = id;
    this.nombre = nombre;
    this.insigniaID = insigniaID;
    this.categoriaInicio = categoriaInicio;
    this.categoriaFin = categoriaFin;
    this.tipo = tipo;
  }

  public String getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public String getInsigniaID() {
    return insigniaID;
  }

  public CategoriaDonadorEnum getCategoriaInicio() {
    return categoriaInicio;
  }

  public CategoriaDonadorEnum getCategoriaFin() {
    return categoriaFin;
  }

  public TipoMisionEnum getTipo() {
    return tipo;
  }

  public Integer getCantidadRequerida() {
    return cantidadRequerida;
  }

  public void setCantidadRequerida(Integer cantidadRequerida) {
    if (cantidadRequerida != null && cantidadRequerida < 1) {
      throw new IllegalArgumentException(
              "La cantidad requerida de donaciones debe ser al menos 1"
      );
    }
    this.cantidadRequerida = cantidadRequerida;
  }

  public int cantidadRequeridaEfectiva() {
    return cantidadRequerida != null
            ? cantidadRequerida
            : EstrategiaDonacionesExitosas.CANTIDAD_POR_DEFECTO;
  }
}