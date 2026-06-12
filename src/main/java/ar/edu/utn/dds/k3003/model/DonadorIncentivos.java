package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DonadorIncentivos {

  @Id
  private String id; // es el donadorID externo

  @ManyToOne
  private Mision misionEnCurso;

  @ManyToMany
  private List<Insignia> insignias;

  public DonadorIncentivos() {
    this.insignias = new ArrayList<>();
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
}