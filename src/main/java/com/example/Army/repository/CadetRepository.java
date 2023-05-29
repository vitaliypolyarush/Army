package com.example.Army.repository;
import com.example.Army.model.Cadet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CadetRepository extends JpaRepository<Cadet,Long> {}
