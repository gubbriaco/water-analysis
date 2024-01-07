package it.progetto.iotserver.services;

import it.progetto.iotserver.entities.Device;
import it.progetto.iotserver.entities.Misuration;
import it.progetto.iotserver.repositories.MisurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MisurationService {

    @Autowired
    private MisurationRepository misurationRepository;

    @Transactional(readOnly = true)
    public Page<Misuration> getAll(Pageable pageable){
        return misurationRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Misuration getByID(String id){
        return misurationRepository.findById(Long.parseLong(id)).get();
    }
    @Transactional(readOnly = true)
    public List<Misuration> getByDevice(Device device){
        return misurationRepository.findByDevice(device);
    }
    @Transactional
    public Long saveMisuration(Misuration misuration){
        return misurationRepository.save(misuration).getId();
    }
    @Transactional
    public void deleteMisuration(Misuration misuration){
        misurationRepository.delete(misuration);
    }
    @Transactional(readOnly = true)
    public boolean existsById(String id){
        return misurationRepository.existsById(Long.parseLong(id));
    }
}
