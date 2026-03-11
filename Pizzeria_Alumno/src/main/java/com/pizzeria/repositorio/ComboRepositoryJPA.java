package com.pizzeria.repositorio;

import com.pizzeria.modelo.Combo;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Combo.
 *
 * Usa EntityManager para guardar, buscar y eliminar combos
 * en la base de datos H2 a través de Hibernate.
 */
public class ComboRepositoryJPA {

    private EntityManager em;

    public ComboRepositoryJPA(EntityManager em) {
        this.em = em;
    }

    /**
     * Guarda un combo en la base de datos.
     * Si no tiene id -> persist (nuevo)
     * Si tiene id -> merge (actualización)
     */
    public Combo guardar(Combo combo) {

        em.getTransaction().begin();

        if (combo.getId() == null) {
            em.persist(combo);
        } else {
            combo = em.merge(combo);
        }

        em.getTransaction().commit();

        return combo;
    }

    /**
     * Busca un combo por su ID
     */
    public Optional<Combo> buscarPorId(Long id) {

        Combo combo = em.find(Combo.class, id);
        return Optional.ofNullable(combo);
    }

    /**
     * Lista todos los combos
     */
    public List<Combo> listarTodos() {

        return em.createQuery(
                        "SELECT c FROM Combo c", Combo.class)
                .getResultList();
    }

    /**
     * Elimina un combo por ID
     */
    public void eliminar(Long id) {

        em.getTransaction().begin();

        Combo combo = em.find(Combo.class, id);

        if (combo != null) {
            em.remove(combo);
        }

        em.getTransaction().commit();
    }
}