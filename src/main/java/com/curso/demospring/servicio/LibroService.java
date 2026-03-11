package com.curso.demospring.servicio;
import com.curso.demospring.modelo.Libro;
import com.curso.demospring.repositorio.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    @Autowired
    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public List<Libro> obtenerLibrosAutores(String autor) {
        return libroRepository.findByAutor(autor);
    }

    public List<Libro> findPrecioLessThan(Double precioMaximo) {
        return libroRepository.findByPrecioLessThan(precioMaximo);
    }

    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }
}