package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario saveUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public List<Usuario> mostrarUsuario() {
        return usuarioRepository.findAll();
    }
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

}
