package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class Fachada implements FachadaIncentivos {

  private DonadorRepository repo = new DonadorRepository();

  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;

  private FachadaDonaciones fachadaDonaciones;

  public void agregarDonacionADonador(
          String donadorID,
          Donacion donacion
  ) {

    DonadorIncentivos donador =
            repo.buscar(donadorID);

    if (donador == null) {

      donador = new DonadorIncentivos(
              donadorID
      );

      repo.guardar(donador);
    }

    donador.agregarDonacion(donacion);
  }

  @Override
  public void setFachadaDonadoresYEntidades(
          FachadaDonadoresYEntidades fachada
  ) {
    this.fachadaDonadoresYEntidades = fachada;
  }

  @Override
  public void setFachadaDonaciones(
          FachadaDonaciones fachadaDonaciones
  ) {
    this.fachadaDonaciones = fachadaDonaciones;
  }

  @Override
  public InsigniaDTO agregarInsignia(
          InsigniaDTO insignia
  ) {
    if (insignia == null || insignia.id() != null) {
      throw new RuntimeException();
    }
    return new InsigniaDTO(
            UUID.randomUUID().toString(),
            insignia.nombre(),
            insignia.descripcion()
    );
  }

  @Override
  public MisionDTO agregarMision(
          MisionDTO mision
  ) {
    if (mision == null || mision.id() != null) {
      throw new RuntimeException();
    }
    return new MisionDTO(
            UUID.randomUUID().toString(),
            mision.nombre(),
            mision.insigniaID(),
            mision.categoriaInicio(),
            mision.categoriaFin(),
            mision.tipo()
    );
  }

  @Override
  public void asignarInsigniaADonador(
          String donadorId,
          InsigniaDTO insigniaDTO
  ) {
    if (insigniaDTO == null) {
      throw new RuntimeException();
    }
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(
              donadorId
      );
    } catch (DonadorNoEncontradoException e) {
      throw new RuntimeException();
    }
    DonadorIncentivos d = repo.buscar(donadorId);
    if (d == null) {
      d = new DonadorIncentivos(donadorId);
      repo.guardar(d);
    }
    d.agregarInsignia(
            new Insignia(
                    insigniaDTO.id(),
                    insigniaDTO.nombre(),
                    insigniaDTO.descripcion()
            )
    );
  }

  @Override
  public List<InsigniaDTO> getInsigniasDeDonador(
          String donadorId
  ) {
    DonadorIncentivos d = repo.buscar(donadorId);
    if (d == null) {
      throw new RuntimeException();
    }
    return d.getInsignias()
            .stream()
            .map(i -> new InsigniaDTO(
                    i.getId(),
                    i.getNombre(),
                    i.getDescripcion()
            ))
            .toList();
  }

  @Override
  public void asignarMisionADonador(
          String donadorID,
          MisionDTO misionDTO
  ) {
    if (misionDTO == null) {
      throw new RuntimeException();
    }
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(
              donadorID
      );
    } catch (DonadorNoEncontradoException e) {
      throw new RuntimeException();
    }
    DonadorIncentivos d = repo.buscar(donadorID);
    if (d == null) {
      d = new DonadorIncentivos(donadorID);
      repo.guardar(d);
    }
    d.setMisionEnCurso(
            new Mision(
                    misionDTO.id(),
                    misionDTO.nombre(),
                    misionDTO.insigniaID(),
                    misionDTO.categoriaInicio(),
                    misionDTO.categoriaFin(),
                    TipoMisionEnum.valueOf(
                            misionDTO.tipo().name()
                    )
            )
    );
  }

  @Override
  public MisionDTO getMisionEnCursoDeDonador(
          String donadorID
  ) {
    DonadorIncentivos d = repo.buscar(donadorID);
    if (d == null) {
      throw new RuntimeException();
    }
    if (d.getMisionEnCurso() == null) {
      return null;
    }
    Mision m = d.getMisionEnCurso();
    return new MisionDTO(
            m.getId(),
            m.getNombre(),
            m.getInsigniaID(),
            m.getCategoriaInicio(),
            m.getCategoriaFin(),
            ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum.valueOf(
                    m.getTipo().name()
            )
    );
  }

  @Override
  public void procesarDonador(
          String donadorID
  ) throws NoSuchElementException {
    if (donadorID == null) {
      throw new RuntimeException();
    }
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(
              donadorID
      );
    } catch (DonadorNoEncontradoException e) {
      throw new RuntimeException();
    }
    if (!repo.existe(donadorID)) {
      repo.guardar(
              new DonadorIncentivos(donadorID)
      );
    }
    DonadorIncentivos donador =
            repo.buscar(donadorID);
    Mision mision =
            donador.getMisionEnCurso();
    if (mision == null) {
      return;
    }
    ProcesadorMisiones procesador =
            new ProcesadorMisiones();
    boolean cumplida =
            procesador.procesar(
                    mision,
                    donador.getDonaciones()
            );
    if (cumplida) {
      donador.setCategoria(
              mision.getCategoriaFin()
      );
      donador.agregarInsignia(
              new Insignia(
                      mision.getInsigniaID(),
                      "Insignia ganada",
                      "Otorgada por completar misión"
              )
      );
    }
  }
}


