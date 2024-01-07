package it.progetto.iotserver.payload.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMisurationRequest {
    @NotBlank(message = "Il campo deviceAddress non può essere vuoto")
    private String deviceAddress;
    @NotBlank(message = "Il campo temperature non può essere vuoto")
    private double temperature;
    @NotBlank(message = "Il campo dissolvedMetal non può essere vuoto")
    private double dissolvedMetals;
    @NotBlank(message = "Il campo ph non può essere vuoto")
    private double ph;
}
