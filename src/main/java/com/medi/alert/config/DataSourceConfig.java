package com.medi.alert.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean(name = "h2DataSource")
    @Qualifier("h2DataSource")
    public DataSource h2DataSource() {
        try {
            return DataSourceBuilder.create()
                    .driverClassName("org.h2.Driver")
                    .url("jdbc:h2:file:./data/h2db/db/medialert_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
                    .username("medialert")
                    .password("")
                    .type(com.zaxxer.hikari.HikariDataSource.class)
                    .build();
        } catch (Exception e) {
            // Gérer l'erreur de création du DataSource H2
            System.err.println("Erreur lors de la création du DataSource H2 : " + e.getMessage());
            return null;
        }
    }

    @Bean(name = "sqlDataSource")
    @Qualifier("sqlDataSource")
    public DataSource sqlDataSource() {
        try {
            return DataSourceBuilder.create()
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .url("jdbc:mysql://medifox.ktc-center.net:3306/medifox")
                    .username("medifox")
                    .password("23%H5kqq2")
                    .type(com.zaxxer.hikari.HikariDataSource.class)
                    .build();
        } catch (Exception e) {
            // Gérer l'erreur de création du DataSource SQL
            System.err.println("Erreur lors de la création du DataSource SQL : " + e.getMessage());
            return null;
        }
    }
}