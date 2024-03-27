package com.medi.alert;

import com.medi.alert.service.EmploiTempsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
public class MediAlertApplication {

	@Autowired
	private EmploiTempsService emploiTempsService;

	public static void main(String[] args) {
		SpringApplication.run(MediAlertApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeData() {
		return args -> {
			// Démarrer l'initialisation des données dans un thread séparé
			CompletableFuture.runAsync(() -> {
				try {
					emploiTempsService.insererDonneesFictives().join();
					// Une fois les données insérées, afficher un message
					System.out.println("Initialisation des données terminée.");
				} catch (Exception e) {
					// Capturer l'exception et afficher un message d'erreur
					System.err.println("Erreur lors de l'initialisation des données : " + e.getMessage());
				}
			});
		};
	}

}
