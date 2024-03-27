package com.medi.alert.service;

import com.medi.alert.entity.EmploiTemps;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class EmploiTempsService {

    private final DataSource sqlDataSource;
    private JdbcTemplate jdbcTemplate;
    private final Executor asyncExecutor = Executors.newCachedThreadPool();

    @Autowired
    public EmploiTempsService(@Qualifier("sqlDataSource") DataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
    }

    @PostConstruct
    private void initializeJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(sqlDataSource);
    }

    // Méthode pour récupérer tous les emplois de temps depuis la base de données
    public CompletableFuture<List<EmploiTemps>> getAllEmploiTemps() {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM emploi_temps";
            return jdbcTemplate.query(sql, (resultSet) -> {
                List<EmploiTemps> emploiTempsList = new ArrayList<>();

                while (resultSet.next()) {
                    EmploiTemps emploiTemps = new EmploiTemps();
                    emploiTemps.setId(resultSet.getLong("id"));
                    emploiTemps.setInfirmiere(resultSet.getString("infirmiere"));
                    emploiTemps.setDateDebut(resultSet.getTimestamp("date_debut"));
                    emploiTemps.setDateFin(resultSet.getTimestamp("date_fin"));

                    emploiTempsList.add(emploiTemps);
                }

                return emploiTempsList;
            });
        }, asyncExecutor);
    }

    public CompletableFuture<Void> insererDonneesFictives() {
        return CompletableFuture.supplyAsync(() -> {
            String countSql = "SELECT COUNT(*) FROM emploi_temps";
            int count = jdbcTemplate.queryForObject(countSql, Integer.class);

            if (count == 0) {
                List<Object[]> donnees = List.of(
                        new Object[]{"Infirmiere 1", Timestamp.valueOf("2024-03-27 08:00:00"), Timestamp.valueOf("2024-03-27 12:00:00")},
                        new Object[]{"Infirmiere 2", Timestamp.valueOf("2024-03-27 13:00:00"), Timestamp.valueOf("2024-03-27 17:00:00")},
                        new Object[]{"Infirmiere 3", Timestamp.valueOf("2024-03-28 09:00:00"), Timestamp.valueOf("2024-03-28 13:00:00")}
                        // Ajoutez d'autres données ici jusqu'à 10
                );

                String insertSql = "INSERT INTO emploi_temps (infirmiere, date_debut, date_fin) VALUES (?, ?, ?)";

                donnees.forEach(donnee -> jdbcTemplate.update(insertSql, donnee));

                System.out.println("Données insérées avec succès dans la table emploi_temps.");
            } else {
                System.out.println("La table emploi_temps contient déjà des enregistrements, aucune donnée n'a été ajoutée.");
            }

            return null;
        }, asyncExecutor);
    }

    public CompletableFuture<Void> verifyData() {
        return CompletableFuture.runAsync(() -> {
            String sql = "SELECT * FROM emploi_temps";
            jdbcTemplate.query(sql, (resultSet) -> {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String infirmiere = resultSet.getString("infirmiere");
                    Timestamp dateDebut = resultSet.getTimestamp("date_debut");
                    Timestamp dateFin = resultSet.getTimestamp("date_fin");

                    System.out.println("ID: " + id + ", Infirmière: " + infirmiere + ", Date début: " + dateDebut + ", Date fin: " + dateFin);
                }
            });
        }, asyncExecutor);
    }
}