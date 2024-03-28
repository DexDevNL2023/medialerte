package com.medi.alert.service;

import com.medi.alert.dto.Infirmiere;
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
public class InfirmiereService {

    private final DataSource sqlDataSource;
    private final Executor asyncExecutor = Executors.newCachedThreadPool();
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InfirmiereService(@Qualifier("sqlDataSource") DataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
    }

    @PostConstruct
    private void initializeJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(sqlDataSource);
    }

    // Méthode pour récupérer tous l'infirmière en fonction de son id ' depuis la base de données
    public CompletableFuture<Infirmiere> getInfirmiereById(int infirmiereId) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM infirmiere WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{infirmiereId}, (resultSet, rowNum) -> {
                Infirmiere infirmiere = new Infirmiere();
                infirmiere.setId(resultSet.getInt("id"));
                infirmiere.setNoms(resultSet.getString("noms"));
                infirmiere.setAdresse(resultSet.getString("adresse"));
                infirmiere.setTelephone(resultSet.getString("telephone"));
                infirmiere.setEmail(resultSet.getString("email"));
                infirmiere.setSpecialiteId(resultSet.getInt("specialite_id"));
                infirmiere.setSexe(resultSet.getString("sexe"));
                return infirmiere;
            });
        }, asyncExecutor);
    }
}