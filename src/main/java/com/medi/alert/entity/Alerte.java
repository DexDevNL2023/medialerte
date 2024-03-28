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
    @JoinColumn(name = "rendez_vous_id")
    private RendezVous rendezVous;

    private String title;
    private String message;
}