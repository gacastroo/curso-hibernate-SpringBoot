package com.curso.demospring.controlador;
import com.curso.demospring.modelo.Producto;
import com.curso.demospring.servicio.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controlador REST para Producto.
 *
 * @RestController = esta clase maneja peticiones HTTP y devuelve JSON.
 * @RequestMapping("/api/productos") = todas las rutas empiezan con /api/productos.
 *
 * Spring convierte los objetos Java a JSON automaticamente (usando Jackson,
 * que viene incluido en spring-boot-starter-web).
 *
 * Recuerdan Gson del dia 7? Aqui NO necesitan Gson. Jackson hace lo mismo
 * pero de forma automatica.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService servicio;

    public ProductoController(ProductoService servicio) {
        this.servicio = servicio;
    }

    /**
     * GET /api/productos
     * Devuelve la lista de todos los productos en JSON.
     */
    @GetMapping
    public List<Producto> listarTodos() {
        return servicio.listarTodos();
    }

    /**
     * GET /api/productos/{id}
     * Devuelve un producto por su id.
     * Si no existe, devuelve 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/productos
     * Crea un producto nuevo. El JSON viene en el body de la peticion.
     *
     * Ejemplo de JSON que se envia:
     * {
     *     "nombre": "Margarita",
     *     "precio": 8.50,
     *     "categoria": "CLASICA"
     * }
     */
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return servicio.guardar(producto);
    }

    /**
     * PUT /api/productos/{id}
     * Actualiza un producto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                               @RequestBody Producto producto) {
        return servicio.buscarPorId(id)
                .map(existente -> {
                    producto.setId(id);
                    return ResponseEntity.ok(servicio.guardar(producto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/productos/{id}
     * Elimina un producto por su id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (servicio.buscarPorId(id).isPresent()) {
            servicio.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}