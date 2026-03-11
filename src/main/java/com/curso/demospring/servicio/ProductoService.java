package com.curso.demospring.servicio;
import com.curso.demospring.modelo.Producto;
import com.curso.demospring.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de Producto.
 *
 * @Service le dice a Spring: "crea una instancia de esta clase y ponla
 * disponible para inyeccion". Cuando alguien necesite un ProductoService,
 * Spring se lo da automaticamente.
 *
 * Fijense: el constructor recibe ProductoRepository.
 * Spring ve que ProductoRepository es un bean (porque extiende JpaRepository)
 * y se lo inyecta automaticamente. Nosotros NO hacemos "new".
 */
@Service
public class ProductoService {

    private final ProductoRepository repositorio;

    // Spring inyecta el repositorio aqui automaticamente
    public ProductoService(ProductoRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * Lista todos los productos.
     */
    public List<Producto> listarTodos() {
        return repositorio.findAll();
    }

    /**
     * Busca un producto por id.
     */
    public Optional<Producto> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    /**
     * Guarda un producto (nuevo o existente).
     * Si tiene id -> UPDATE. Si no tiene id -> INSERT.
     */
    public Producto guardar(Producto producto) {
        return repositorio.save(producto);
    }

    /**
     * Elimina un producto por id.
     */
    public void eliminar(Long id) {
        repositorio.deleteById(id);
    }
}