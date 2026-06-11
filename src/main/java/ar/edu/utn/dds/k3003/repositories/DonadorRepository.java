package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonadorRepository
        extends JpaRepository<DonadorIncentivos, String> {

}