
package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Reserva;
import com.example.model.Usuario;
import com.example.service.ReservaService;
import com.example.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "https://librotech.vercel.app")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ReservaService reservaService;

    @PostMapping("/registro")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> loginUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.findUsuarioByUsername(usuario.getUsername());
        if (usuarioExistente != null && usuarioExistente.getPassword().equals(usuario.getPassword())) {
            return ResponseEntity.ok(usuarioExistente);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping
    public List<Usuario>mostrarListaUsuario(){
        return usuarioService.mostrarUsuario();
    }
    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {

        usuarioService.deleteUsuario(id);

    }


}
