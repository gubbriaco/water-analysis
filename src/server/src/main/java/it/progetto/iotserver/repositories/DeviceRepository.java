package it.progetto.iotserver.repositories;

import it.progetto.iotserver.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long> {
    Optional<Device> findByAddress(String address);
    boolean existsByAddress(String address);
}
