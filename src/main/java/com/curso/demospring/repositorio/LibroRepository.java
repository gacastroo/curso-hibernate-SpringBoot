package com.curso.demospring.repositorio;
import com.curso.demospring.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByAutor(String autor);
    List<Libro> findByPrecioLessThan(Double precioMaximo);
}
