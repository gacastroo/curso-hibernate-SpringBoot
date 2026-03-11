package com.curso.demospring.modelo;
// ║  Crear la entidad Libro con las 4 capas de Spring Boot        ║
//║  y exponer una API REST con estos endpoints:                  ║
//║                                                               ║
//║    GET    /api/libros              → listar todos              ║
//║    GET    /api/libros/{id}         → buscar por id             ║
//║    GET    /api/libros/autor/{autor}→ buscar por autor           ║
//║    POST   /api/libros              → crear un libro            ║
//║    DELETE /api/libros/{id}         → eliminar un libro         ║
//║                                                               ║
//║  Campos de Libro: id, titulo, autor, anioPublicacion, precio

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Libro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private int anioPublicacion;
    private Double precio;
}
