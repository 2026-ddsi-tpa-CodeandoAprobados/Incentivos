package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
public class DonadorIncentivos {

  @Id
  private String id;

  @ManyToOne
  private Mision misionEnCurso;

  @ManyToMany
  private List<Insignia> insignias;

  @ManyToMany
  private List<Mision> misionesCompletadas;

  public DonadorIncentivos() {
    this.insignias = new ArrayList<>();
    this.misionesCompletadas = new ArrayList<>();
  }

  public DonadorIncentivos(String id) {
    this();
    this.id = id;
  }

  public String getId() {
    return id;
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

  public void quitarInsignia(Insignia insignia) {
    this.insignias.remove(insignia);
  }

  public List<Mision> getMisionesCompletadas() {
    return misionesCompletadas;
  }

  public void completarMision(Mision mision) {
    this.misionesCompletadas.add(mision);
  }

  public void quitarMisionCompletada(Mision mision) {
    this.misionesCompletadas.remove(mision);
  }
}