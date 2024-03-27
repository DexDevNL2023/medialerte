package com.medi.alert.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "alertes")
@Data
public class Alerte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "emploi_temps_id")
    private EmploiTemps emploiTemps;
}