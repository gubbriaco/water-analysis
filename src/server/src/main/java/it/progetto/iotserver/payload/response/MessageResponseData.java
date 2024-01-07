package it.progetto.iotserver.payload.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class MessageResponseData {
    public String message;
    public Data data;
    public MessageResponseData(String message, String id){
        this.message=message;
        this.data= new Data(id);
    }
}
