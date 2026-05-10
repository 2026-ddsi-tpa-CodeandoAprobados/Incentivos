package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Insignia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsigniaRepository {

    private Map<String, Insignia> data =
            new HashMap<>();

    public void guardar(
            Insignia insignia
    ) {

        data.put(
                insignia.getId(),
                insignia
        );
    }

    public Insignia buscar(
            String id
    ) {

        return data.get(id);
    }

    public List<Insignia> buscarTodos() {

        return new ArrayList<>(
                data.values()
        );
    }
}