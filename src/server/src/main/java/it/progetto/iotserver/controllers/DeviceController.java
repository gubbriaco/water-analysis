package it.progetto.iotserver.controllers;

import it.progetto.iotserver.entities.Device;
import it.progetto.iotserver.entities.Misuration;
import it.progetto.iotserver.payload.request.CreateDeviceRequest;
import it.progetto.iotserver.payload.response.MessageResponse;
import it.progetto.iotserver.payload.response.MessageResponseData;
import it.progetto.iotserver.services.DeviceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device")
@CrossOrigin("http://:3000")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @GetMapping
    public ResponseEntity getAll(){
        List<Device> deviceList= deviceService.getAll();
        return new ResponseEntity<>(deviceList,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity getByID(@PathVariable @Pattern(regexp = "\\d+", message="Il parametro id deve essere un numero.") String id){
        return new ResponseEntity(deviceService.getByID(id),HttpStatus.OK);
    }
    @GetMapping("/pag")
    public ResponseEntity getAllDevicePag(@RequestParam(defaultValue = "1") @Pattern(regexp = "\\d+", message = "Il parametro page deve essere un numero") String page,
                                  @RequestParam(defaultValue = "25") @Pattern(regexp = "\\d+", message = "Il parametro limit deve essere un numero") String limit,
                                  @RequestParam(defaultValue = "id") String orderBy){
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by(orderBy));
        Page<Device> pageList= deviceService.getAllPag(pageable);
        return new ResponseEntity(pageList, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity saveDevice(@RequestBody@Valid CreateDeviceRequest createDeviceRequest){
        long id = deviceService.saveDevice(new Device(createDeviceRequest.getName(),createDeviceRequest.getAddress(), createDeviceRequest.getEnvironment()));
        return new ResponseEntity(new MessageResponseData("Device salvato correttamente",String.valueOf(id)),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity deleteDevice(@PathVariable @Pattern(regexp = "\\d+", message="Il parametro id deve essere un numero.") String id){
        Device device= deviceService.getByID(id);
        deviceService.deleteDevice(device);
        return new ResponseEntity(new MessageResponse("Device rimosso correttamente"),HttpStatus.OK);
    }
}
