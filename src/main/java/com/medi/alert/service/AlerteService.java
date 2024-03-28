package com.medi.alert.service;

import com.medi.alert.entity.Alerte;
import com.medi.alert.entity.RendezVous;
import com.medi.alert.exceptions.ResourceNotFoundException;
import com.medi.alert.repository.AlerteRepository;
import com.medi.alert.repository.RendezVousRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlerteService {
    private final AlerteRepository alerteRepository;
    private final RendezVousRepository rendezVousRepository;

    public AlerteService(AlerteRepository alerteRepository, RendezVousRepository rendezVousRepository) {
        this.alerteRepository = alerteRepository;
        this.rendezVousRepository = rendezVousRepository;
    }

    public List<Alerte> getAllAlertes() {
        return alerteRepository.findAll();
    }

    public void addAlerte(RendezVous dto) {
        Alerte alerte = new Alerte();
        RendezVous rendezVous = new RendezVous();
        rendezVous.setId(dto.getId());
        rendezVous.setPatientId(dto.getPatientId());
        rendezVous.setInfirmiereId(dto.getInfirmiereId());
        rendezVous.setJour(dto.getJour());
        rendezVous.setHeure(dto.getHeure());
        rendezVous.setEtat(dto.isEtat());
        rendezVous.setRetard(dto.getRetard());
        rendezVous.setDateRetard(dto.getDateRetard());
        rendezVous.setHeureRetard(dto.getHeureRetard());
        alerte.setRendezVous(rendezVous);
        alerteRepository.save(alerte);
    }

    public void updateAlerte(RendezVous dto) {
        Alerte alerte = alerteRepository.findByRendezVousId(dto.getId());
        if (alerte != null) {
            RendezVous rendezVous = rendezVousRepository.findById(dto.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Le Rendez-Vous avec l'id " + dto.getId() + " n'existe pas")
            );
            // Mettre à jour les informations de l'alerte si nécessaire
            alerte.setRendezVous(rendezVous);
            alerteRepository.save(alerte);
        }
    }
}