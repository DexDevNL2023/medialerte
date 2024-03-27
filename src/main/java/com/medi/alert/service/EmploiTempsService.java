package com.medi.alert.service;

import com.medi.alert.dto.EmploiTempsDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploiTempsService {

    private final JdbcTemplate jdbcTemplate;

    public EmploiTempsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EmploiTempsDto> getAllEmploiTemps() {
        String sql = "SELECT * FROM emploi_temps";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(EmploiTempsDto.class));
    }
}