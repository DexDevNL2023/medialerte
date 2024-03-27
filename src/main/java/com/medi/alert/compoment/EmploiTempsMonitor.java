package com.medi.alert.compoment;

import com.medi.alert.dto.EmploiTempsDto;
import com.medi.alert.entity.Alerte;
import com.medi.alert.service.AlerteService;
import com.medi.alert.service.EmailService;
import com.medi.alert.service.EmploiTempsService;
import com.medi.alert.service.SmsService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class EmploiTempsMonitor {

    private final AlerteService emploiTempsHistoryService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final EmploiTempsService emploiTempsService;

    public EmploiTempsMonitor(AlerteService emploiTempsHistoryService, EmailService emailService, SmsService smsService, EmploiTempsService emploiTempsService) {
        this.emploiTempsHistoryService = emploiTempsHistoryService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.emploiTempsService = emploiTempsService;
    }

    @Scheduled(fixedRate = 30000) // Vérifier toutes les 30 secondes
    @Transactional
    public void checkModifications() {
        List<EmploiTempsDto> currentData = emploiTempsService.getAllEmploiTemps();
        List<Alerte> previousData = emploiTempsHistoryService.getAllAlertes();

        // Vérifier les nouvelles données et les modifications
        for (EmploiTempsDto currentEmploiTempsDto : currentData) {
            Alerte previousEmploiTemps = findAlerteById(previousData, currentEmploiTempsDto.getId());
            if (previousEmploiTemps == null) {
                // Donnée nouvelle
                emploiTempsHistoryService.addAlerte(currentEmploiTempsDto);
                notifyModification(currentEmploiTempsDto);
            } else if (!currentEmploiTempsDto.equals(previousEmploiTemps.getEmploiTemps())) {
                // Modification détectée
                emploiTempsHistoryService.updateAlerte(currentEmploiTempsDto);
                notifyModification(currentEmploiTempsDto);
            }
        }
    }

    // Méthode pour rechercher une donnée dans la liste des données d'historique par ID
    private Alerte findAlerteById(List<Alerte> alerteList, Long id) {
        for (Alerte alerte : alerteList) {
            if (alerte.getEmploiTemps().getId().equals(id)) {
                return alerte;
            }
        }
        return null;
    }

    // Méthode pour notifier les modifications par email et SMS
    private void notifyModification(EmploiTempsDto emploiTempsDto) {
        System.out.println("Modification détectée dans l'emploi du temps des infirmières !");
        // Exemple d'envoi de notification par email
        emailService.sendEmail("patient@example.com", "Alerte : Notification d'emploi du temps modifié", "Le nouvel emploi du temps est disponible.");
        // Exemple d'envoi de notification par SMS via Twilio
        smsService.sendSms("+1234567890", "Alerte : Nouvel emploi du temps disponible.");
    }
}