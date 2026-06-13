package ar.edu.utn.dds.k3003.services;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DetalleProductoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.clients.CategoriasClient;
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

import org.springframework.stereotype.Service;






@Service
public class IncentivosService {
    private DonadorRepository donadorRepository;

    private MisionRepository misionRepository;

    private InsigniaRepository insigniaRepository;
    private final DonadoresClient donadoresClient;
    private final CategoriasClient categoriasClient;

    public IncentivosService(
            DonadorRepository donadorRepository,
            MisionRepository misionRepository,
            InsigniaRepository insigniaRepository,
            CategoriasClient categoriasClient,
            DonadoresClient donadoresClient
    ) {
        this.donadorRepository = donadorRepository;
        this.misionRepository = misionRepository;
        this.insigniaRepository = insigniaRepository;
        this.categoriasClient = categoriasClient;
        this.donadoresClient = donadoresClient;
    }
    // =========================
    // INSIGNIAS
    // =========================

    public void guardarInsignia(Insignia insignia) {

        insigniaRepository.save(insignia);
    }

    public Insignia buscarInsignia(
            String id
    ) {

        return insigniaRepository
                .findById(id)
                .orElse(null);
    }

    public List<Insignia> buscarTodasLasInsignias() {

        return insigniaRepository.findAll();
    }

    // =========================
    // MISIONES
    // =========================

    public void guardarMision(Mision mision) {
        misionRepository.save(mision);
    }

    public Mision buscarMision(String id) {

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
        DonadorIncentivos donador = obtenerDonador(donadorID);
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
            Insignia insignia = buscarInsignia(
                    mision.getInsigniaID()
            );
            if (insignia != null) {
                donador.agregarInsignia(insignia);
            }
            // Actualiza la categoría del donador en el otro módulo
            donadoresClient.actualizarCategoria(
                    donadorID,
                    new CategoriaRequest(mision.getCategoriaFin())
            );
            donador.setMisionEnCurso(null);
            donadorRepository.save(donador);
        }
    }


    public void reset() {
        donadorRepository.deleteAll();
        misionRepository.deleteAll();
        insigniaRepository.deleteAll();
    }
}

