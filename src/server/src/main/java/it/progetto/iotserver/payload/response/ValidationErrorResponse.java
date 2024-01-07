package it.progetto.iotserver.payload.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ValidationErrorResponse {
    public ValidationErrorResponse(String message, String code, HttpStatus status, Map<String, String> errors) {
        this.message = message;
        this.status=status;
        this.errors = errors;
        this.code = code;
    }
    private String code;
    private HttpStatus status;
    private String message;
    private Map<String,String> errors;
}
