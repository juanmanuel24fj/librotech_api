package com.example.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.model.Libro;
import com.example.model.Reserva;
import com.example.model.Usuario;
import com.example.repository.LibroRepository;
import com.example.repository.ReservaRepository;
import com.example.repository.UsuarioRepository;

@Service
public class ReservaService {
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public void reservarLibro(Long libroId, Long usuarioId) {
        if (reservaRepository.existsByUsuarioId(usuarioId)) {
            throw new RuntimeException("El usuario ya tiene una reserva activa.");
        }

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (libro.getEjemplaresDisponibles() > 0) {
            libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
            libroRepository.save(libro);

            Reserva reserva = new Reserva();
            reserva.setLibro(libro);
            reserva.setUsuario(usuario);
            reserva.setFechaReserva(LocalDate.now());
            reserva.setFechaDevolucion(LocalDate.now().plusWeeks(2));
            reservaRepository.save(reserva);
        } else {
            throw new RuntimeException("No hay ejemplares disponibles.");
        }
    }

    public boolean devolverLibro(Long reservaId) {
        // Encontrar la reserva por ID
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Obtener el libro asociado a la reserva
        Libro libro = reserva.getLibro();

        // Incrementar el contador de ejemplares disponibles
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);

        // Guardar los cambios en la base de datos
        libroRepository.save(libro);
        reservaRepository.delete(reserva);

        return true;
    }
    @Scheduled(cron = "0 0 0 * * *") // Ejecuta cada día a medianoche
    public void eliminarUsuariosConReservasVencidas() {
        LocalDate fechaActual = LocalDate.now();
        List<Reserva> reservasVencidas = reservaRepository.findAllByFechaDevolucionBefore(fechaActual);
        for (Reserva reserva : reservasVencidas) {
            Usuario usuario = reserva.getUsuario();
            usuarioRepository.delete(usuario);
            reservaRepository.delete(reserva);
        }
    }

}
