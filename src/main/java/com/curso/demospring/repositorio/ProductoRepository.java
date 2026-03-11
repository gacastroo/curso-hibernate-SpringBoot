package com.curso.demospring.repositorio;
import com.curso.demospring.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de Producto.
 *
 * Eso es TODO. Una interfaz que extiende JpaRepository.
 * Spring genera automaticamente la implementacion con estos metodos:
 *
 *   findAll()          -> SELECT * FROM productos
 *   findById(Long id)  -> SELECT * FROM productos WHERE id = ?
 *   save(Producto p)   -> INSERT o UPDATE segun si tiene id o no
 *   deleteById(Long id)-> DELETE FROM productos WHERE id = ?
 *   count()            -> SELECT COUNT(*) FROM productos
 *   existsById(Long id)-> SELECT COUNT(*) > 0 WHERE id = ?
 *
 * Ademas, pueden agregar metodos custom por convencion de nombre:
 *   List<Producto> findByCategoria(String categoria);
 *   List<Producto> findByPrecioGreaterThan(double precio);
 *   Optional<Producto> findByNombre(String nombre);
 *
 * Spring lee el nombre del metodo y genera la consulta SQL.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}