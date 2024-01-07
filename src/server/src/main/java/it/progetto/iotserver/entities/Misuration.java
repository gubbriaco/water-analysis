package it.progetto.iotserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "misuration")
@ToString
@NoArgsConstructor
public class Misuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "id", nullable = false, unique = true, length = 50)
    private Long id;
    @Column(name = "date",nullable = false)
    private Date date;
    @Column(name = "temperature")
    private double temperature;
    @Column(name = "dissolvedMetal")
    private double dissolvedMetal;
    @Column(name = "ph",nullable = false)
    private double ph;

    @ManyToOne
    @JoinColumn(name = "fk_device")
    @JsonIgnore
    private Device device;

    public Misuration(Device device,double temperature,double dissolvedMetal,double ph){
        this.date=new Date();
        this.device=device;
        this.temperature=temperature;
        this.dissolvedMetal=dissolvedMetal;
        this.ph=ph;
    }
}
