package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Libro;
import java.util.List;


@Repository
public interface LibroRepository extends JpaRepository<Libro, Long>{

	Libro findByTitulo(String titulo);
	
	List<Libro> findByAutor(String autor);
}
