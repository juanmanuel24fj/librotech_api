package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Usuario;
import java.util.List;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Usuario findByUsername(String username);
}
