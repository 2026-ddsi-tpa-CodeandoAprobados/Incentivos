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
}