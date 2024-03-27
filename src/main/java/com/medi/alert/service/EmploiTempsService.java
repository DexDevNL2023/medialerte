package com.medi.alert.service;

import com.medi.alert.entity.EmploiTemps;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
//public class EmploiTempsService {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public EmploiTempsService() {
//        this.jdbcTemplate = new JdbcTemplate(mysqlDataSource());
//    }
//
//    // Méthode pour récupérer tous les emplois de temps depuis la base de données
//    public List<EmploiTemps> getAllEmploiTemps() {
//        String sql = "SELECT * FROM emploi_temps";
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            EmploiTemps emploiTemps = new EmploiTemps();
//            emploiTemps.setId(rs.getLong("id"));
//            emploiTemps.setInfirmiere(rs.getString("infirmiere"));
//            emploiTemps.setDateDebut(rs.getDate("date_debut"));
//            emploiTemps.setDateFin(rs.getDate("date_fin"));
//            return emploiTemps;
//        });
//    }
//    @Bean
//    public DataSource mysqlDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://192.168.1.40:3306/medialert");
//        dataSource.setUsername("root");
//        dataSource.setPassword("2+2Font4");
//        return dataSource;
//    }
//}

@Service
public class EmploiTempsService {

    // Méthode pour récupérer tous les emplois de temps depuis la base de données
    public List<EmploiTemps> getAllEmploiTemps() {
        return new ArrayList<>();
    }
}