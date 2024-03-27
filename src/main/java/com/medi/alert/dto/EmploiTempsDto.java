package com.medi.alert.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EmploiTempsDto {
    private Long id;
    private String infirmiere;
    private Date dateDebut;
    private Date dateFin;
}