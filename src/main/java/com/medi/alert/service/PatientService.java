package com.medi.alert.service;

import com.medi.alert.dto.Patient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class PatientService {

    private final DataSource sqlDataSource;
    private final Executor asyncExecutor = Executors.newCachedThreadPool();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PatientService(@Qualifier("sqlDataSource") DataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
    }

    @PostConstruct
    private void initializeJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(sqlDataSource);
    }

    // Méthode pour récupérer tous le patient en fonction de son id depuis la base de données
    public CompletableFuture<Patient> getPatientById(int patientId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM patient WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{patientId}, (resultSet, rowNum) -> {
                Patient patient = new Patient();
                patient.setId(resultSet.getInt("id"));
                patient.setNoms(resultSet.getString("noms"));
                patient.setAdresse(resultSet.getString("adresse"));
                patient.setTelephone(resultSet.getString("telephone"));
                patient.setEmail(resultSet.getString("email"));
                patient.setSexe(resultSet.getString("sexe"));
                return patient;
            });
        }, asyncExecutor);
    }
}