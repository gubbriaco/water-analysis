package it.progetto.iotserver.exception;

import it.progetto.iotserver.repositories.MisurationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MisurationNotFoundException extends RuntimeException{
    public MisurationNotFoundException(String exception){
        super(exception);
    }
}
