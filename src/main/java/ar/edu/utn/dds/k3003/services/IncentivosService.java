package ar.edu.utn.dds.k3003.services;

import ar.edu.utn.dds.k3003.model.Donacion;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.ProcesadorMisiones;
import ar.edu.utn.dds.k3003.repositories.DonadorRepository;
import ar.edu.utn.dds.k3003.repositories.InsigniaRepository;
import ar.edu.utn.dds.k3003.repositories.MisionRepository;

public class IncentivosService {

    private DonadorRepository donadorRepository;

    private MisionRepository misionRepository;

    private InsigniaRepository insigniaRepository;

    public IncentivosService(
            DonadorRepository donadorRepository,
            MisionRepository misionRepository,
            InsigniaRepository insigniaRepository
    ) {

        this.donadorRepository = donadorRepository;

        this.misionRepository = misionRepository;

        this.insigniaRepository = insigniaRepository;
    }

    // =========================
    // INSIGNIAS
    // =========================

    public void guardarInsignia(
            Insignia insignia
    ) {

        insigniaRepository.guardar(insignia);
    }

    public Insignia buscarInsignia(
            String id
    ) {

        return insigniaRepository.buscar(id);
    }

    // =========================
    // MISIONES
    // =========================

    public void guardarMision(
            Mision mision
    ) {

        misionRepository.guardar(mision);
    }

    public Mision buscarMision(
            String id
    ) {

        return misionRepository.buscar(id);
    }

    // =========================
    // DONADORES
    // =========================

    public DonadorIncentivos obtenerDonador(
            String id
    ) {

        DonadorIncentivos donador =
                donadorRepository.buscar(id);

        if (donador == null) {
            throw new RuntimeException();
        }

        return donador;
    }

    public DonadorIncentivos obtenerOCrearDonador(
            String id
    ) {

        DonadorIncentivos donador =
                donadorRepository.buscar(id);

        if (donador == null) {

            donador = new DonadorIncentivos(id);

            donadorRepository.guardar(donador);
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

        DonadorIncentivos donador =
                obtenerOCrearDonador(donadorID);

        donador.agregarInsignia(insignia);
    }

    public void asignarMision(
            String donadorID,
            Mision mision
    ) {

        DonadorIncentivos donador =
                obtenerOCrearDonador(donadorID);

        donador.setMisionEnCurso(mision);
    }

    // =========================
    // DONACIONES
    // =========================

    public void agregarDonacion(
            String donadorID,
            Donacion donacion
    ) {

        DonadorIncentivos donador =
                obtenerOCrearDonador(donadorID);

        donador.agregarDonacion(donacion);
    }

    // =========================
    // PROCESAMIENTO
    // =========================

    public void procesarDonador(
            String donadorID
    ) {

        DonadorIncentivos donador =
                obtenerDonador(donadorID);

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

            Insignia insignia =
                    buscarInsignia(
                            mision.getInsigniaID()
                    );

            if (insignia != null) {

                donador.agregarInsignia(
                        insignia
                );
            }
        }
    }
}