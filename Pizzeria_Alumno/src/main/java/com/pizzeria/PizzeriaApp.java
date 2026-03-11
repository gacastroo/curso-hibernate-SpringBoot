package com.pizzeria;

import com.pizzeria.modelo.*;
import com.pizzeria.repositorio.ClienteRepositoryJPA;
import com.pizzeria.repositorio.ComboRepositoryJPA;
import com.pizzeria.repositorio.PizzaRepositoryJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class PizzeriaApp {

    public static void main(String[] args) {

        System.out.println("=============================================");
        System.out.println("   PIZZERIA JAVA - Version 5 (Hibernate)");
        System.out.println("=============================================\n");

        // ==========================================================
        // 1. INICIALIZAR HIBERNATE
        // ==========================================================

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pizzeria-pu");
        EntityManager em = emf.createEntityManager();

        System.out.println("Hibernate inicializado. Base de datos H2 lista.\n");

        // ==========================================================
        // 2. CREAR REPOSITORIOS
        // ==========================================================

        PizzaRepositoryJPA pizzaRepo = new PizzaRepositoryJPA(em);
        ClienteRepositoryJPA clienteRepo = new ClienteRepositoryJPA(em);
        ComboRepositoryJPA comboRepo = new ComboRepositoryJPA(em);

        // ==========================================================
        // 3. CREAR PIZZAS
        // ==========================================================

        System.out.println("--- CREAR PIZZAS ---\n");

        Pizza margarita = new Pizza("Margarita", 8.50, Pizza.Categoria.CLASICA);
        Pizza carnivora = new Pizza("Carnivora", 12.00, Pizza.Categoria.CLASICA);
        Pizza trufa = new Pizza("Trufa Negra", 18.50, Pizza.Categoria.GOURMET);
        Pizza vegana = new Pizza("Mediterranea Vegana", 11.00, Pizza.Categoria.VEGANA);

        pizzaRepo.guardar(margarita);
        pizzaRepo.guardar(carnivora);
        pizzaRepo.guardar(trufa);
        pizzaRepo.guardar(vegana);

        System.out.println("Pizzas guardadas:");
        pizzaRepo.listarTodas().forEach(System.out::println);

        // ==========================================================
        // 4. CREAR CLIENTES
        // ==========================================================

        System.out.println("\n--- CREAR CLIENTES ---\n");

        Cliente ana = new Cliente("Ana Lopez", TipoCliente.PERSONA);
        ana.setEmail("ana.lopez@gmail.com");

        Cliente carlos = new Cliente("Carlos Martinez", TipoCliente.PERSONA);

        Cliente banco = new Cliente("Banco Santander", TipoCliente.EMPRESA);
        banco.setEmail("pedidos@santander.es");
        banco.setTelefono("911234567");

        clienteRepo.guardar(ana);
        clienteRepo.guardar(carlos);
        clienteRepo.guardar(banco);

        clienteRepo.buscarTodos().forEach(System.out::println);

        // ==========================================================
        // 5. CREAR PEDIDOS
        // ==========================================================

        System.out.println("\n--- CREAR PEDIDOS ---\n");

        try {

            em.getTransaction().begin();

            Pedido pedido1 = new Pedido(ana);
            pedido1.agregarPizza(margarita);
            pedido1.agregarPizza(trufa);
            em.persist(pedido1);

            Pedido pedido2 = new Pedido(carlos);
            pedido2.agregarPizza(carnivora);
            pedido2.agregarPizza(vegana);
            pedido2.agregarPizza(margarita);
            em.persist(pedido2);

            Pedido pedidoBanco = new Pedido(banco);
            pedidoBanco.agregarPizza(margarita);
            pedidoBanco.agregarPizza(margarita);
            pedidoBanco.agregarPizza(carnivora);
            em.persist(pedidoBanco);

            em.getTransaction().commit();

            System.out.println("Pedidos guardados:");
            System.out.println(pedido1);
            System.out.println(pedido2);
            System.out.println(pedidoBanco);

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            System.err.println("Error al crear pedidos: " + e.getMessage());
        }

        // ==========================================================
        // 6. CREAR COMBO
        // ==========================================================

        System.out.println("\n--- CREAR COMBO ---\n");

        Combo familiar = new Combo("Combo Familiar", "2 pizzas", 15.0);

        familiar.agregarPizza(margarita);
        familiar.agregarPizza(carnivora);

        comboRepo.guardar(familiar);

        System.out.println("Combo guardado:");
        System.out.println(familiar);
        System.out.println("Ahorro del combo: " + familiar.getAhorro());

        // ==========================================================
        // 7. LISTAR COMBOS
        // ==========================================================

        System.out.println("\n--- LISTAR COMBOS ---\n");

        List<Combo> combos = comboRepo.listarTodos();
        combos.forEach(System.out::println);

        // ==========================================================
        // 8. CONSULTAR PEDIDOS
        // ==========================================================

        System.out.println("\n--- CONSULTAR PEDIDOS ---\n");

        List<Pedido> todosPedidos = em.createQuery(
                        "SELECT p FROM Pedido p", Pedido.class)
                .getResultList();

        for (Pedido p : todosPedidos) {
            System.out.printf(
                    "Pedido #%d - Cliente: %s - Pizzas: %d - Total: %.2f%n",
                    p.getId(),
                    p.getCliente().getNombre(),
                    p.getCantidadItems(),
                    p.getTotal()
            );
        }

        // ==========================================================
        // 9. ELIMINAR
        // ==========================================================

        System.out.println("\n--- ELIMINAR ---\n");

        Pizza temporal = new Pizza("Pizza Temporal", 5.00, Pizza.Categoria.CLASICA);
        pizzaRepo.guardar(temporal);

        System.out.println("Pizzas antes: " + pizzaRepo.listarTodas().size());
        pizzaRepo.eliminar(temporal.getId());
        System.out.println("Pizzas despues: " + pizzaRepo.listarTodas().size());

        // ==========================================================
        // 10. CERRAR
        // ==========================================================

        em.close();
        emf.close();

        System.out.println("\n=============================================");
        System.out.println("   Hibernate cerrado. Datos persistidos.");
        System.out.println("=============================================");
    }
}