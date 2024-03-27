package com.medi.alert.repository;

import com.medi.alert.entity.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    Alerte findByEmploiTempsId(Long emploiTempsId);
}