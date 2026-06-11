package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.services.IncentivosService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
public class Fachada implements FachadaIncentivos {

  private final IncentivosService service;
  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;
  private FachadaDonaciones fachadaDonaciones;

  public Fachada(IncentivosService service) {
    this.service = service;
  }

  @Override
  public void setFachadaDonadoresYEntidades(FachadaDonadoresYEntidades fachada) {
    this.fachadaDonadoresYEntidades = fachada;
  }

  @Override
  public void setFachadaDonaciones(FachadaDonaciones fachadaDonaciones) {
    this.fachadaDonaciones = fachadaDonaciones;
  }

  public void agregarDonacionADonador(String donadorID, Donacion donacion) {
    service.agregarDonacion(donadorID, donacion);
  }

  @Override
  public InsigniaDTO agregarInsignia(InsigniaDTO insignia) {
    if (insignia == null || insignia.id() != null) {
      throw new RuntimeException();
    }

    Insignia nueva = new Insignia(
            UUID.randomUUID().toString(),
            insignia.nombre(),
            insignia.descripcion()
    );

    service.guardarInsignia(nueva);

    return new InsigniaDTO(nueva.getId(), nueva.getNombre(), nueva.getDescripcion());
  }

  @Override
  public MisionDTO agregarMision(MisionDTO mision) {
    if (mision == null || mision.id() != null) {
      throw new RuntimeException();
    }

    Mision nueva = new Mision(
            UUID.randomUUID().toString(),
            mision.nombre(),
            mision.insigniaID(),
            mision.categoriaInicio(),
            mision.categoriaFin(),
            TipoMisionEnum.valueOf(mision.tipo().name())
    );

    service.guardarMision(nueva);

    return toDTO(nueva);
  }

  @Override
  public void asignarInsigniaADonador(String donadorId, InsigniaDTO insigniaDTO) {
    if (insigniaDTO == null) {
      throw new RuntimeException();
    }

    validarDonador(donadorId);

    Insignia insignia = service.buscarInsignia(insigniaDTO.id());

    if (insignia == null) {
      insignia = new Insignia(
              insigniaDTO.id(),
              insigniaDTO.nombre(),
              insigniaDTO.descripcion()
      );
      service.guardarInsignia(insignia);
    }

    service.asignarInsignia(donadorId, insignia);
  }

  @Override
  public List<InsigniaDTO> getInsigniasDeDonador(String donadorId) {
    DonadorIncentivos d = service.obtenerDonador(donadorId);

    return d.getInsignias()
            .stream()
            .map(i -> new InsigniaDTO(i.getId(), i.getNombre(), i.getDescripcion()))
            .toList();
  }

  @Override
  public void asignarMisionADonador(String donadorID, MisionDTO misionDTO) {
    if (misionDTO == null) {
      throw new RuntimeException();
    }

    validarDonador(donadorID);

    Mision mision = service.buscarMision(misionDTO.id());

    if (mision == null) {
      mision = new Mision(
              misionDTO.id(),
              misionDTO.nombre(),
              misionDTO.insigniaID(),
              misionDTO.categoriaInicio(),
              misionDTO.categoriaFin(),
              TipoMisionEnum.valueOf(misionDTO.tipo().name())
      );
      service.guardarMision(mision);
    }

    service.asignarMision(donadorID, mision);
  }

  @Override
  public MisionDTO getMisionEnCursoDeDonador(String donadorID) {
    DonadorIncentivos d = service.obtenerDonador(donadorID);

    if (d.getMisionEnCurso() == null) {
      return null;
    }

    return toDTO(d.getMisionEnCurso());
  }

  @Override
  public void procesarDonador(String donadorID) throws NoSuchElementException {
    if (donadorID == null) {
      throw new RuntimeException();
    }

    validarDonador(donadorID);
    service.procesarDonador(donadorID);
  }

  public List<InsigniaDTO> getInsignias() {
    return service.buscarTodasLasInsignias()
            .stream()
            .map(i -> new InsigniaDTO(i.getId(), i.getNombre(), i.getDescripcion()))
            .toList();
  }

  public InsigniaDTO getInsignia(String id) {
    Insignia insignia = service.buscarInsignia(id);

    if (insignia == null) {
      throw new RuntimeException();
    }

    return new InsigniaDTO(insignia.getId(), insignia.getNombre(), insignia.getDescripcion());
  }

  public List<MisionDTO> getMisiones() {
    return service.buscarTodasLasMisiones()
            .stream()
            .map(this::toDTO)
            .toList();
  }

  public MisionDTO getMision(String id) {
    Mision mision = service.buscarMision(id);

    if (mision == null) {
      throw new RuntimeException();
    }

    return toDTO(mision);
  }

  private void validarDonador(String donadorID) {
    if (fachadaDonadoresYEntidades == null) {
      return;
    }

    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (DonadorNoEncontradoException e) {
      throw new RuntimeException();
    }
  }

  private MisionDTO toDTO(Mision m) {
    return new MisionDTO(
            m.getId(),
            m.getNombre(),
            m.getInsigniaID(),
            m.getCategoriaInicio(),
            m.getCategoriaFin(),
            ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum.valueOf(m.getTipo().name())
    );
  }
}