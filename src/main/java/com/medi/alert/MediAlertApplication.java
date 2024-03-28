package com.medi.alert;

import com.medi.alert.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAutoConfiguration
public class MediAlertApplication {

	@Autowired
    private RendezVousService rendezVousService;

	public static void main(String[] args) {
		SpringApplication.run(MediAlertApplication.class, args);
	}
}
