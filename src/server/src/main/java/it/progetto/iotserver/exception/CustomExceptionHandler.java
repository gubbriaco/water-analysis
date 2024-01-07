package it.progetto.iotserver.exception;


import it.progetto.iotserver.payload.response.ErrorResponse;
import it.progetto.iotserver.payload.response.ValidationErrorResponse;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            FieldError f = ex.getBindingResult().getFieldErrors(fieldName).get(0);
            errors.put(fieldName, f.getDefaultMessage());
        }
        ValidationErrorResponse error = new ValidationErrorResponse("Validazione fallita", "400",
                HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public final ResponseEntity<Object> handleSizeLimitExceededException(SizeLimitExceededException ex) {
        ErrorResponse error = new ErrorResponse("Il file deve essere massimo di 10 MB", HttpStatus.BAD_REQUEST, "400");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}