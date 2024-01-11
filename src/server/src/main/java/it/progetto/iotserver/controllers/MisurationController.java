package it.progetto.iotserver.controllers;

import it.progetto.iotserver.entities.*;
import it.progetto.iotserver.exception.DeviceNotFoundException;
import it.progetto.iotserver.exception.MisurationNotFoundException;
import it.progetto.iotserver.payload.request.CreateMisurationRequest;
import it.progetto.iotserver.payload.response.MessageResponseData;
import it.progetto.iotserver.services.DeviceService;
import it.progetto.iotserver.services.MisurationService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/misuration")
@CrossOrigin("http://localhost:3000")
public class MisurationController {
    @Autowired
    private MisurationService misurationService;
    @Autowired
    private DeviceService deviceService;
    @GetMapping
    public ResponseEntity getAll(){
        return new ResponseEntity<>(misurationService.getAll(),HttpStatus.OK);
    }

    @GetMapping("/pag")
    public ResponseEntity getAllPag(
                                 @RequestParam(defaultValue = "1") @Pattern(regexp = "\\d+", message = "Il parametro page deve essere un numero") String page,
                                 @RequestParam(defaultValue = "25") @Pattern(regexp = "\\d+", message = "Il parametro limit deve essere un numero") String limit,
                                 @RequestParam(defaultValue = "place") String orderBy){
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(orderBy));
        Page<Misuration> pageList= misurationService.getAllPag(pageable);
        return new ResponseEntity(pageList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable @Pattern(regexp = "\\d+", message="Il parametro id deve essere un numero.") String id){
        if(!misurationService.existsById(id)) throw new MisurationNotFoundException("Misurazione non trovata");
        Misuration misuration = misurationService.getByID(id);
        return new ResponseEntity<>(misuration,HttpStatus.OK);
    }
    @GetMapping("/device/{id}")
    public ResponseEntity getByDevice(@PathVariable @Pattern(regexp = "\\d+", message="Il parametro id deve essere un numero.") String id){
        Device device= deviceService.getByID(id);
        List<Misuration> misurationList= misurationService.getByDevice(device);
        return new ResponseEntity(misurationList,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity saveMisuration(@RequestBody CreateMisurationRequest createMisurationRequest){
        if(!deviceService.existsByAddress(createMisurationRequest.getDeviceAddress())) throw new DeviceNotFoundException("Dispositivo sconosciuto: "+createMisurationRequest.getDeviceAddress());
        Device device= deviceService.getByAddress(createMisurationRequest.getDeviceAddress());
        Misuration misuration= new Misuration(device,createMisurationRequest.getTemperature(),
                createMisurationRequest.getDissolvedMetals(),
                createMisurationRequest.getPh());
        long id=misurationService.saveMisuration(misuration);
        return new ResponseEntity<>(new MessageResponseData("Misurazione creata correttamente",String.valueOf(id)),HttpStatus.OK);
    }

}
