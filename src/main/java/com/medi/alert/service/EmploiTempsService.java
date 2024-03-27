package com.medi.alert.service;

import com.medi.alert.entity.EmploiTemps;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class EmploiTempsService {

    private final DataSource sqlDataSource;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public EmploiTempsService(@Qualifier("sqlDataSource") DataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
    }

    @PostConstruct
    private void initializeJdbcTemplate() {
        this.jdbcTemplate = new JdbcTemplate(sqlDataSource);
    }

    // Méthode pour récupérer tous les emplois de temps depuis la base de données
    public List<EmploiTemps> getAllEmploiTemps() {
        String sql = "SELECT * FROM emploi_temps";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmploiTemps emploiTemps = new EmploiTemps();
            emploiTemps.setId(rs.getLong("id"));
            emploiTemps.setInfirmiere(rs.getString("infirmiere"));
            emploiTemps.setDateDebut(rs.getDate("date_debut"));
            emploiTemps.setDateFin(rs.getDate("date_fin"));
            return emploiTemps;
        });
    }
}