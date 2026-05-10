package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import java.util.HashMap;
import java.util.Map;

public class DonadorRepository {

    private Map<String, DonadorIncentivos> data = new HashMap<>();

    public DonadorIncentivos buscar(String id) {
        return data.get(id);
    }

    public void guardar(DonadorIncentivos donador) {
        data.put(donador.getId(), donador);
    }

    public boolean existe(String id) {
        return data.containsKey(id);
    }
}