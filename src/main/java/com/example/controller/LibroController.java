package com.example.controller;

import com.example.model.Libro;
import com.example.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = "https://librotech.vercel.app")
public class LibroController {
	
    @Autowired
    private LibroService servicioLibros;

    @GetMapping
    public List<Libro> obtenerTodosLosLibros() {
        return servicioLibros.obtenerTodosLosLibros();
    }

    @GetMapping("/{id}")
    public Libro obtenerLibroPorId(@PathVariable Long id) {
        return servicioLibros.obtenerLibroPorId(id);
    }

    @GetMapping("/titulo/{titulo}")
    public Libro obtenerLibroPorTitulo(@PathVariable String titulo) {
        return servicioLibros.obtenerLibroPorTitulo(titulo);
    }

    @GetMapping("/autor/{autor}")
    public List<Libro> obtenerLibrosPorAutor(@PathVariable String autor) {
        return servicioLibros.obtenerLibrosPorAutor(autor);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Libro crearLibro(@RequestPart("libro") Libro libro, @RequestPart("imagen") MultipartFile imagen) {
        // Guardar la imagen y obtener el nombre del archivo
        String imagenNombre = servicioLibros.guardarImagen(imagen);
        libro.setImagen(imagenNombre);

        return servicioLibros.crearLibro(libro);
    }

    @GetMapping("/images/{nombreImagen}")
    public ResponseEntity<Resource> obtenerImagenLibro(@PathVariable String nombreImagen) {
        Resource imagen = servicioLibros.obtenerImagenLibro(nombreImagen);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
    }
}
