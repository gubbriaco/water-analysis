package it.progetto.iotserver.payload.response;


import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
   
    private String message;
    private HttpStatus status;
    private String code;
}