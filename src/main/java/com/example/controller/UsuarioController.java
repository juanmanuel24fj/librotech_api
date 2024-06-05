
package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.exception.ExceptionCredentialNotValid;
import com.example.model.LoginCredential;
import com.example.model.TokenDto;
import com.example.model.Usuario;
import com.example.service.ReservaService;
import com.example.service.UsuarioService;
import com.example.utility.TokenUtils;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "https://librotech.vercel.app")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ReservaService reservaService;
    
    @Autowired
	private AuthenticationManager authenticationManager;

    @PostMapping("/registro")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    @GetMapping
    public List<Usuario>mostrarListaUsuario(){
        return usuarioService.mostrarUsuario();
    }
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {

        usuarioService.deleteUsuario(id);

    }
    
    @PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginCredential loginRequest) {

		Authentication authentication;
		//Si el usuario y el password que le paso son los adecuados me 
		// devuele un autentication. Si no lo encuentra, lanza una exception
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
	
		} catch (Exception e) {
			throw new ExceptionCredentialNotValid(e.getMessage());
		}
		
		Usuario user = (Usuario)authentication.getPrincipal();
		String jwt = TokenUtils.generateToken(loginRequest.getUsername(), user.getEmail(), user.getRole(), user.getId());
		
		TokenDto dtoToken = new TokenDto(jwt);
		
		return ResponseEntity.ok(dtoToken);
	}


}
