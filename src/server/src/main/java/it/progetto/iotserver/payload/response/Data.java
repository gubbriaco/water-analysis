package it.progetto.iotserver.payload.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Data {
    private String id;
    public Data(String id){
        this.id=id;
    }
}
