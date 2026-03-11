package com.curso.demospring.controlador;

import com.curso.demospring.modelo.Libro;
import com.curso.demospring.servicio.LibroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService servicioLibro;

    public LibroController(LibroService servicioLibro) {
        this.servicioLibro = servicioLibro;
    }

    @GetMapping
    public List<Libro> obtenerLibros() {
        return servicioLibro.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        return servicioLibro.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/libros/autor/{autor}
     * Obtiene todos los libros de un autor específico.
     */
    @GetMapping("/autor/{autor}")
    public List<Libro> obtenerLibrosAutor(@PathVariable String autor) {
        return servicioLibro.obtenerLibrosAutores(autor);
    }

    /**
     * POST /api/libros
     * Crea un libro nuevo. El JSON viene en el body de la petición.
     *
     * Ejemplo de JSON:
     * {
     *     "nombre": "Margarita",
     *     "precio": 8.50,
     *     "categoria": "CLASICA"
     * }
     */

    @GetMapping("/baratos/{precioMaximo}")
    public List<Libro> findPrecioLessThan(@PathVariable Double precioMaximo) {
        return servicioLibro.findPrecioLessThan(precioMaximo);
    }

    @PostMapping
    public Libro crear(@RequestBody Libro libro) {
        return servicioLibro.guardar(libro);
    }

    @PostMapping("/varios")
    public List<Libro> guardarvarios(@RequestBody List<Libro> libros) {
        return libros.stream()
                .map(servicioLibro::guardar)
                .collect(Collectors.toList());
    }
    /**
     * PUT /api/libros/{id}
     * Actualiza un libro existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id,
                                            @RequestBody Libro libro) {
        return servicioLibro.buscarPorId(id)
                .map(existente -> {
                    libro.setId(id);
                    return ResponseEntity.ok(servicioLibro.guardar(libro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/libros/{id}
     * Elimina un libro por su id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (servicioLibro.buscarPorId(id).isPresent()) {
            servicioLibro.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}