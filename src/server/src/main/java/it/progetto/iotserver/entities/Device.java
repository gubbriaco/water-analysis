package it.progetto.iotserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "device")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id", nullable = false, unique = true, length = 50)
    private long id;

    @Column(name = "name",nullable = false)
    @Basic
    private String name;
    @Column(name = "address",nullable = false)
    @Basic
    private String address;
    @Column(name = "environment",nullable = false)
    private String environment;

    @OneToMany(mappedBy = "device")
    private List<Misuration> listaMisuration;


    public Device(String name, String address,String environment){
        this.name=name;
        this.address=address;
        this.environment=environment;
    }

}
