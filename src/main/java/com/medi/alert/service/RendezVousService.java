package com.medi.alert.service;

import com.medi.alert.entity.RendezVous;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class RendezVousService {

    private final DataSource sqlDataSource;
    private JdbcTemplate jdbcTemplate;
    private final Executor asyncExecutor = Executors.newCachedThreadPool();

    @Autowired
    public RendezVousService(@Qualifier("sqlDataSource") DataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
    }

    @PostConstruct
    private void initializeJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(sqlDataSource);
    }

    // Méthode pour récupérer tous les rendez-vous depuis la base de données
    public CompletableFuture<List<RendezVous>> getAllRendezVous() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM rendez_vous";
            return jdbcTemplate.query(sql, (resultSet) -> {
                List<RendezVous> rendezVousList = new ArrayList<>();

                while (resultSet.next()) {
                    RendezVous rendezVous = new RendezVous();
                    rendezVous.setId(resultSet.getInt("id"));
                    rendezVous.setPatientId(resultSet.getInt("patient_id"));
                    rendezVous.setInfirmiereId(resultSet.getInt("infirmiere_id"));
                    rendezVous.setJour(resultSet.getDate("jour").toLocalDate());
                    rendezVous.setHeure(resultSet.getTime("heure").toLocalTime());
                    rendezVous.setEtat(resultSet.getBoolean("etat"));
                    rendezVous.setRetard(resultSet.getInt("retard"));
                    rendezVous.setDateRetard(resultSet.getDate("date_retard").toLocalDate());
                    rendezVous.setHeureRetard(resultSet.getTime("heure_retard").toLocalTime());
                    rendezVousList.add(rendezVous);
                }

                return rendezVousList;
            });
        }, asyncExecutor);
    }
}