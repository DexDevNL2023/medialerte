package com.medi.alert.service;

import com.medi.alert.entity.Alerte;
import com.medi.alert.entity.EmploiTemps;
import com.medi.alert.exceptions.ResourceNotFoundException;
import com.medi.alert.repository.AlerteRepository;
import com.medi.alert.repository.EmploiTempsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlerteService {
    private final AlerteRepository alerteRepository;
    private final EmploiTempsRepository emploiTempsRepository;

    public AlerteService(AlerteRepository alerteRepository, EmploiTempsRepository emploiTempsRepository) {
        this.alerteRepository = alerteRepository;
        this.emploiTempsRepository = emploiTempsRepository;
    }

    public List<Alerte> getAllAlertes() {
        return alerteRepository.findAll();
    }

    public void addAlerte(EmploiTemps dto) {
        Alerte alerte = new Alerte();
        EmploiTemps emploiTemps = new EmploiTemps();
        emploiTemps.setInfirmiere(dto.getInfirmiere());
        emploiTemps.setDateDebut(dto.getDateDebut());
        emploiTemps.setDateFin(dto.getDateFin());
        alerte.setEmploiTemps(emploiTemps);
        alerteRepository.save(alerte);
    }

    public void updateAlerte(EmploiTemps dto) {
        Alerte alerte = alerteRepository.findByEmploiTempsId(dto.getId());
        if (alerte != null) {
            EmploiTemps emploiTemps = emploiTempsRepository.findById(dto.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("L'emploi de temps avec l'id " + dto.getId() + " n'existe pas")
            );
            // Mettre à jour les informations de l'alerte si nécessaire
            alerte.setEmploiTemps(emploiTemps);
            alerteRepository.save(alerte);
        }
    }
}