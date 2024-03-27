package com.medi.alert.compoment;

import com.medi.alert.entity.Alerte;
import com.medi.alert.entity.EmploiTemps;
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

    private final AlerteService alerteService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final EmploiTempsService emploiTempsService;

    public EmploiTempsMonitor(AlerteService alerteService, EmailService emailService, SmsService smsService, EmploiTempsService emploiTempsService) {
        this.alerteService = alerteService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.emploiTempsService = emploiTempsService;
    }

    @Scheduled(fixedRate = 30000) // Vérifier toutes les 30 secondes
    @Transactional
    public void checkModifications() {
        try {
            List<EmploiTemps> currentData = emploiTempsService.getAllEmploiTemps();
            List<Alerte> previousData = alerteService.getAllAlertes();

            // Vérifier les nouvelles données et les modifications
            for (EmploiTemps currentEmploiTemps : currentData) {
                Alerte previousEmploiTemps = findAlerteById(previousData, currentEmploiTemps.getId());
                if (previousEmploiTemps == null) {
                    // Donnée nouvelle
                    alerteService.addAlerte(currentEmploiTemps);
                    notifyModification(currentEmploiTemps);
                } else if (!currentEmploiTemps.equals(previousEmploiTemps.getEmploiTemps())) {
                    // Modification détectée
                    alerteService.updateAlerte(currentEmploiTemps);
                    notifyModification(currentEmploiTemps);
                }
            }
        } catch (Exception e) {
            // Gérer l'exception sans arrêter l'application
            System.err.println("Une erreur s'est produite lors de la vérification des modifications : " + e.getMessage());
            // Vous pouvez choisir de journaliser l'erreur ou d'envoyer une notification par email ou SMS
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
    private void notifyModification(EmploiTemps currentEmploiTemps) {
        System.out.println("Modification détectée dans l'emploi du temps des infirmières !");
        // Exemple d'envoi de notification par email
        emailService.sendEmail("patient@example.com", "Alerte : Notification d'emploi du temps modifié", "Le nouvel emploi du temps est disponible.");
        // Exemple d'envoi de notification par SMS via Twilio
        smsService.sendSms("+1234567890", "Alerte : Nouvel emploi du temps disponible.");
    }
}