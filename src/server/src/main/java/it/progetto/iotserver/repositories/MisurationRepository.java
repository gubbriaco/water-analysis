package it.progetto.iotserver.repositories;

import it.progetto.iotserver.entities.Device;
import it.progetto.iotserver.entities.Misuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MisurationRepository extends JpaRepository<Misuration,Long> {
    List<Misuration> findByDevice(Device device);
}
