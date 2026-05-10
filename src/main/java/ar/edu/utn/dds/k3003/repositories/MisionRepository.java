package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Mision;

import java.util.HashMap;
import java.util.Map;

public class MisionRepository {

    private Map<String, Mision> data = new HashMap<>();

    public void guardar(Mision mision) {
        data.put(mision.getId(), mision);
    }

    public Mision buscar(String id) {
        return data.get(id);
    }
}