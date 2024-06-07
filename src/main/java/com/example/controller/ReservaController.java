package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.Reserva;
import com.example.repository.ReservaRepository;
import com.example.service.ReservaService;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "https://librotech.vercel.app")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;


     @PostMapping("/reservar/{libroId}")
    public ResponseEntity<?> reservarLibro(@PathVariable Long libroId) {
        try {
            String username = getUsernameFromSecurityContext();
            if (username == null) {
                return ResponseEntity.status(401).body("No autorizado");
            }

            Usuario usuario = usuarioService.findByUsername(username);
            if (usuario == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }

            reservaService.reservarLibro(libroId, usuario.getId());
            return ResponseEntity.ok().body("Reserva creada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/devolver/{id}")
    public ResponseEntity<?> devolverLibro(@PathVariable Long id) {
        boolean resultado = reservaService.devolverLibro(id);
        if (resultado) {
            return ResponseEntity.ok().body("Libro devuelto correctamente.");
        } else {
            return ResponseEntity.badRequest().body("Error al devolver el libro.");
        }
    }
    @GetMapping("")
    public List<Reserva> obtenerTodosLosLibros() {
        return reservaRepository.findAll();
    }

}



