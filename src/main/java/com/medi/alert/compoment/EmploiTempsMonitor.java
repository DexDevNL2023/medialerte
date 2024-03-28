package com.medi.alert.compoment;

import com.medi.alert.dto.Infirmiere;
import com.medi.alert.dto.Patient;
import com.medi.alert.entity.Alerte;
import com.medi.alert.entity.RendezVous;
import com.medi.alert.service.*;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@EnableScheduling
public class EmploiTempsMonitor {

    private final AlerteService alerteService;
    private final EmailService emailService;
    private final SmsService smsService;
    private final RendezVousService rendezVousService;
    private final PatientService patientService;
    private final InfirmiereService infirmiereService;

    public EmploiTempsMonitor(AlerteService alerteService, EmailService emailService, SmsService smsService, RendezVousService rendezVousService, PatientService patientService, InfirmiereService infirmiereService) {
        this.alerteService = alerteService;
        this.emailService = emailService;
        this.smsService = smsService;
        this.rendezVousService = rendezVousService;
        this.patientService = patientService;
        this.infirmiereService = infirmiereService;
    }

    @Scheduled(fixedRate = 30000) // Vérifier toutes les 30 secondes
    @Transactional
    public void checkModifications() {
        try {
            CompletableFuture<List<RendezVous>> future = rendezVousService.getAllRendezVous();

            // Attendre que la tâche asynchrone se termine avec une limite de temps de 10 secondes
            List<RendezVous> currentData = future.get(10, TimeUnit.SECONDS);

            // Utilisez currentData comme d'habitude
            List<Alerte> previousData = alerteService.getAllAlertes();

            // Vérifier les nouvelles données et les modifications
            for (RendezVous currentRendezVous : currentData) {
                RendezVous previousRendezVous = findRendezVousByAlerteList(previousData, currentRendezVous.getId());
                if (previousRendezVous == null) {
                    // Donnée nouvelle
                    alerteService.addAlerte(currentRendezVous);
                    notifyModification(currentRendezVous);
                } else if (!currentRendezVous.isEtat() &&
                        currentRendezVous.getRetard() != previousRendezVous.getRetard()) {
                    // Modification détectée
                    alerteService.updateAlerte(currentRendezVous);
                    notifyModification(currentRendezVous);
                }
            }
        } catch (Exception e) {
            // Gérer l'exception sans arrêter l'application
            System.err.println("Une erreur s'est produite lors de la vérification des modifications : " + e.getMessage());
            // Vous pouvez choisir de journaliser l'erreur ou d'envoyer une notification par email ou SMS
        }
    }

    // Méthode pour rechercher une donnée dans la liste des données d'historique par ID
    private RendezVous findRendezVousByAlerteList(List<Alerte> alerteList, int id) {
        for (Alerte alerte : alerteList) {
            if (alerte.getRendezVous().getId() == id) {
                return alerte.getRendezVous();
            }
        }
        return null;
    }

    // Méthode pour notifier les modifications par email et SMS
    private void notifyModification(RendezVous currentRendezVous) {
        System.out.println("Modification détectée dans l'emploi du temps des infirmières !");

        try {
            // Récupérer les informations du patient à partir de son service
            CompletableFuture<Patient> futurePatient = patientService.getPatientById(currentRendezVous.getPatientId());

            // Récupérer les informations de l'infirmière à partir de son service
            CompletableFuture<Infirmiere> futureInfirmiere = infirmiereService.getInfirmiereById(currentRendezVous.getInfirmiereId());

            // Attendre que les tâches asynchrones se terminent avec une limite de temps de 10 secondes
            Patient patient = futurePatient.get(10, TimeUnit.SECONDS);
            Infirmiere infirmiere = futureInfirmiere.get(10, TimeUnit.SECONDS);

            // Vérifier si le patient et l'infirmière existent
            if (patient != null && infirmiere != null) {
                // Exemple d'envoi de notification par email au patient
                if (patient.getEmail() != null) {
                    emailService.sendEmail(patient.getEmail(), "Alerte : Notification de rendez-vous modifié",
                            "Votre rendez-vous avec " + infirmiere.getNoms()
                                    + " a été modifié pour " + currentRendezVous.getDateRetard() + " à " + currentRendezVous.getHeureRetard()
                                    + "\n" +
                                    "Cordialement le service de gestion des rendez-vous");
                }

                // Exemple d'envoi de notification par SMS à l'infirmière
                if (infirmiere.getTelephone() != null) {
                    smsService.sendSms(infirmiere.getTelephone(),
                            "Alerte : Votre rendez-vous avec " + patient.getNoms()
                                    + " a été modifié pour " + currentRendezVous.getDateRetard() + " à " + currentRendezVous.getHeureRetard()
                                    + "\n" +
                                    "Cordialement le service de gestion des rendez-vous");
                }
            } else {
                System.out.println("Impossible de récupérer les informations du patient ou de l'infirmière.");
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.err.println("Une erreur s'est produite lors de la récupération des informations du patient ou de l'infirmière : " + e.getMessage());
            // Gérer l'erreur
        }
    }
}