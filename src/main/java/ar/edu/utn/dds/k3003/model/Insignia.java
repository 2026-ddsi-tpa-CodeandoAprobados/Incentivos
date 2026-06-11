package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Insignia {

  @Id
  private String id;

  private String nombre;

  private String descripcion;

  public Insignia() {
  }

  public Insignia(
          String id,
          String nombre,
          String descripcion
  ) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
  }

  public String getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }
}