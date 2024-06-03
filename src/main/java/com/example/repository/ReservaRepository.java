package com.example.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{

	List<Reserva> findAllByFechaDevolucionBefore(LocalDate fecha);
    List<Reserva> findAllByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
}
