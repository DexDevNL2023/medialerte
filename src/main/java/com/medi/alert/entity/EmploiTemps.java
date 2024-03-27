package com.medi.alert.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "emploi_temps")
@Data
public class EmploiTemps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_debut")
    private Date dateDebut;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_fin")
    private Date dateFin;

    @OneToOne(mappedBy = "emploiTemps", cascade = CascadeType.PERSIST)
    private Alerte alerte;
}