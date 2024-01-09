package it.progetto.iotserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(String exception){
        super(exception);
    }
}
