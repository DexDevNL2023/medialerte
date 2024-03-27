package com.medi.alert.repository;

import com.medi.alert.entity.EmploiTemps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploiTempsRepository extends JpaRepository<EmploiTemps, Long> {
}