package com.project.carwash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.carwash.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

}
