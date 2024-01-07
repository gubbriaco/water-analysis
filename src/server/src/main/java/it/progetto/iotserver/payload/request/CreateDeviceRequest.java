package it.progetto.iotserver.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateDeviceRequest {
    @NotBlank(message = "Il campo name non può essere vuoto")
    private String name;
    @NotBlank(message = "Il campo address non può essere vuoto")
    private String address;
    @NotBlank(message = "Il campo environment non può essere vuoto")
    private String environment;

}
