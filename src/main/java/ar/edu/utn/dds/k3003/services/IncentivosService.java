package ar.edu.utn.dds.k3003.services;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DetalleProductoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;
import ar.edu.utn.dds.k3003.metrics.IncentivosMetrics;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.ProcesadorMisiones;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.repositories.InsigniaRepository;
import ar.edu.utn.dds.k3003.repositories.MisionRepository;
import ar.edu.utn.dds.k3003.clients.DonadoresClient;
import ar.edu.utn.dds.k3003.clients.CategoriaRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;






@Service
public class IncentivosService {
    private static final Logger log =
            LoggerFactory.getLogger(IncentivosService.class);
    private DonadorRepository donadorRepository;
    private MisionRepository misionRepository;
    private final IncentivosMetrics metrics;
    private InsigniaRepository insigniaRepository;
    private final DonadoresClient donadoresClient;
    private final CategoriasClient categoriasClient;

    public IncentivosService(
            DonadorRepository donadorRepository,
            MisionRepository misionRepository,
            InsigniaRepository insigniaRepository,
            CategoriasClient categoriasClient,
            DonadoresClient donadoresClient,
            IncentivosMetrics metrics
    ) {
        this.donadorRepository = donadorRepository;
        this.misionRepository = misionRepository;
        this.insigniaRepository = insigniaRepository;
        this.categoriasClient = categoriasClient;
        this.donadoresClient = donadoresClient;
        this.metrics = metrics;
    }
    // =========================
    // INSIGNIAS
    // =========================

    public void guardarInsignia(Insignia insignia) {
        metrics.registrarInsigniaCreada();
        insigniaRepository.save(insignia);
    }

    public Insignia buscarInsignia(
            String id
    ) {
        metrics.registrarConsultaInsignia();
        return insigniaRepository
                .findById(id)
                .orElse(null);
    }

    public void eliminarInsignia(String id) {
        if (!insigniaRepository.existsById(id)) {
            throw new RuntimeException("Insignia no encontrada");
        }

        insigniaRepository.deleteById(id);
    }

    public List<Insignia> buscarTodasLasInsignias() {

        return insigniaRepository.findAll();
    }

    // =========================
    // MISIONES
    // =========================

    public void guardarMision(Mision mision) {
        metrics.registrarMisionCreada();
        misionRepository.save(mision);
    }

    public void eliminarMision(String id) {
        if (!misionRepository.existsById(id)) {
            throw new RuntimeException("Mision no encontrada");
        }

        misionRepository.deleteById(id);
    }

    public Mision buscarMision(String id) {
        metrics.registrarConsultaMision();
        return misionRepository
                .findById(id)
                .orElse(null);
    }

    public List<Mision> buscarTodasLasMisiones() {

        return misionRepository.findAll();
    }

    // =========================
    // DONADORES
    // =========================

    public DonadorIncentivos obtenerDonador(String id) {
        metrics.registrarConsultaDonador();
        DonadorIncentivos donador =
                donadorRepository
                        .findById(id)
                        .orElse(null);
        if (donador == null) {
            throw new RuntimeException();
        }
        return donador;
    }

    public DonadorIncentivos obtenerOCrearDonador(String id) {

        DonadorIncentivos donador =
                donadorRepository
                        .findById(id)
                        .orElse(null);
        if (donador == null) {
            donador = new DonadorIncentivos(id);
            donadorRepository.save(donador);
        }
        return donador;
    }

    // =========================
    // ASIGNACIONES
    // =========================

    public void asignarInsignia(
            String donadorID,
            Insignia insignia
    ) {
        DonadorIncentivos donador = obtenerOCrearDonador(donadorID);
        donador.agregarInsignia(insignia);
        donadorRepository.save(donador);
    }

    public void asignarMision(
            String donadorID,
            Mision mision
    ) {
        DonadorIncentivos donador = obtenerOCrearDonador(donadorID);
        donador.setMisionEnCurso(mision);
        donadorRepository.save(donador);
    }

    // =========================
    // PROCESAMIENTO
    // =========================
/*
    public void procesarDonador(
            String donadorID,
            List<DonacionDTO> donaciones
    ) {
        DonadorIncentivos donador = obtenerDonador(donadorID);
        Mision mision = donador.getMisionEnCurso();
        if (mision == null) {
            return;
        }
        ProcesadorMisiones procesador = new ProcesadorMisiones();
        boolean cumplida = procesador.procesar(mision, donaciones);
        if (cumplida) {
            Insignia insignia = buscarInsignia(mision.getInsigniaID());
            if (insignia != null) {
                donador.agregarInsignia(insignia);
            }
            donadorRepository.save(donador);
        }
    }
*/
    public void procesarDonador(
            String donadorID,
            List<DonacionDTO> donaciones
    ) {
        metrics.registrarProcesamiento();
        try {
            DonadorIncentivos donador = obtenerDonador(donadorID);
            // Primero verificamos si perdió el progreso
            // de alguna misión que ya había completado.
            boolean perdioProgreso = verificarPerdidaDeProgreso(
                    donador,
                    donaciones
            );
            // Si perdió progreso, ya restauramos la misión anterior.
            // No seguimos procesando en esta ejecución.
            if (perdioProgreso) {
                return;
            }
            Mision mision = donador.getMisionEnCurso();
            if (mision == null) {
                return;
            }
            ProcesadorMisiones procesador = new ProcesadorMisiones();
            boolean cumplida = procesador.procesar(
                    mision,
                    donaciones,
                    categoriasClient
            );
            if (cumplida) {
                metrics.registrarMisionCumplida();
                Insignia insignia = buscarInsignia(
                        mision.getInsigniaID()
                );
                if (insignia != null) {
                    donador.agregarInsignia(insignia);
                    metrics.registrarInsigniaAsignada();
                }
                donadoresClient.actualizarCategoria(
                        donadorID,
                        new CategoriaRequest(mision.getCategoriaFin())
                );
                donador.completarMision(mision);
                donador.setMisionEnCurso(null);
                donadorRepository.save(donador);
            }
        } catch (Exception e) {
            metrics.registrarError();
            throw e;
        }
    }


    private boolean verificarPerdidaDeProgreso(
            DonadorIncentivos donador,
            List<DonacionDTO> donaciones
    ) {
        ProcesadorMisiones procesador = new ProcesadorMisiones();
        List<Mision> misionesCompletadas =
                List.copyOf(donador.getMisionesCompletadas());
        for (Mision mision : misionesCompletadas) {
            // actualmente solo DONACIONES_EXITOSAS.
            if (mision.getTipo()
                    != ar.edu.utn.dds.k3003.model.TipoMisionEnum.DONACIONES_EXITOSAS) {
                continue;
            }
            boolean sigueCumplida = procesador.procesar(
                    mision,
                    donaciones,
                    categoriasClient
            );
            if (!sigueCumplida) {
                log.warn(
                        "PERDIDA DE PROGRESO - Donador {} dejo de cumplir la mision {}. Retrocede de {} a {}",
                        donador.getId(),
                        mision.getNombre(),
                        mision.getCategoriaFin(),
                        mision.getCategoriaInicio()
                );
                Insignia insignia =
                        buscarInsignia(mision.getInsigniaID());
                if (insignia != null) {
                    donador.quitarInsignia(insignia);
                }
                donador.quitarMisionCompletada(mision);
                // la misión vuelve a quedar activa
                donador.setMisionEnCurso(mision);
                // el donador vuelve a la categoría desd la cual había iniciado esta misión
                donadoresClient.actualizarCategoria(
                        donador.getId(),
                        new CategoriaRequest(mision.getCategoriaInicio())
                );
                donadorRepository.save(donador);
                return true;
            }
        }
        return false;
    }

    public void reset() {
        donadorRepository.deleteAll();
        misionRepository.deleteAll();
        insigniaRepository.deleteAll();
    }
}

