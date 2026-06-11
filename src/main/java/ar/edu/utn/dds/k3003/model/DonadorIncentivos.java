package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DonadorIncentivos {

  @Id
  private String id;

  @Enumerated(EnumType.STRING)
  private CategoriaDonadorEnum categoria;

  @ManyToOne
  private Mision misionEnCurso;

  @ManyToMany
  private List<Insignia> insignias;

  @Transient
  private List<Donacion> donaciones;

  public DonadorIncentivos() {
    this.insignias = new ArrayList<>();
    this.donaciones = new ArrayList<>();
  }

  public DonadorIncentivos(String id) {

    this();

    this.id = id;
    this.categoria = CategoriaDonadorEnum.OCASIONAL;
  }

  public String getId() {
    return id;
  }

  public CategoriaDonadorEnum getCategoria() {
    return categoria;
  }

  public void setCategoria(CategoriaDonadorEnum categoria) {
    this.categoria = categoria;
  }

  public Mision getMisionEnCurso() {
    return misionEnCurso;
  }

  public void setMisionEnCurso(Mision misionEnCurso) {
    this.misionEnCurso = misionEnCurso;
  }

  public List<Insignia> getInsignias() {
    return insignias;
  }

  public void agregarInsignia(Insignia insignia) {
    this.insignias.add(insignia);
  }

  public List<Donacion> getDonaciones() {
    return donaciones;
  }

  public void agregarDonacion(Donacion donacion) {
    this.donaciones.add(donacion);
  }
}