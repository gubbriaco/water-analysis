package it.progetto.iotserver.services;

import it.progetto.iotserver.entities.Device;
import it.progetto.iotserver.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public Page<Device> getAllPag(Pageable pageable){
        return deviceRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public List<Device> getAll(){
        return deviceRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Device getByID(String id){
        return deviceRepository.getReferenceById(Long.parseLong(id));
    }
    @Transactional(readOnly = true)
    public Device getByAddress(String address){
        return deviceRepository.findByAddress(address).get();
    }
    @Transactional
    public long saveDevice(Device device){
        return deviceRepository.save(device).getId();
    }
    @Transactional
    public void deleteDevice(Device device){
        deviceRepository.delete(device);
    }
    @Transactional(readOnly = true)
    public boolean existsByAddress(String address){
        return deviceRepository.existsByAddress(address);
    }
}
