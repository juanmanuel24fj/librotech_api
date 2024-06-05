package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exception.ExceptionCredentialNotValid;
import com.example.model.Usuario;
import com.example.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario saveUsuario(Usuario usuario) {
    	usuario.setRole("ROLE_USER");
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(encodedPassword);
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username);
		
		if (usuario !=null) {
			return usuario;
		}else {
			
			throw new ExceptionCredentialNotValid("Usuario no encontrado username: " + username);
		}
	}

}
