package com.medi.alert.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rendez_vous")
@Data
public class RendezVous {

    @Id
    private int id;
    private int patientId;
    private int infirmiereId;
    private LocalDate jour;
    private LocalTime heure;
    private boolean etat;
    private int retard;
    private LocalDate dateRetard;
    private LocalTime heureRetard;

    @OneToOne(mappedBy = "rendezVous", cascade = CascadeType.PERSIST)
    private Alerte alerte;
}