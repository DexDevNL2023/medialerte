package com.medi.alert.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Patient {
    private int id;
    private String noms;
    private String adresse;
    private String telephone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String sexe;
}
