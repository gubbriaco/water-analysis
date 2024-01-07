package it.progetto.iotserver.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageResponse {
    public String message;
    public MessageResponse(String message){
        this.message=message;
    }
}
