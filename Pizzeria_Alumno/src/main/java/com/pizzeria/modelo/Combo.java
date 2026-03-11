package com.pizzeria.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "combos")
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    @ManyToMany
    @JoinTable(
            name = "combo_pizzas",
            joinColumns = @JoinColumn(name = "combo_id"),
            inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private List<Pizza> pizzas = new ArrayList<>();

    private double precioEspecial;

    // Constructor vacio para Hibernate
    protected Combo() {
    }

    public Combo(String nombre, String descripcion, double precioEspecial) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioEspecial = precioEspecial;
    }

    // --- Metodos de negocio ---

    public void agregarPizza(Pizza pizza) {
        this.pizzas.add(pizza);
    }

    public double getAhorro() {
        double precioSinCombo = pizzas.stream()
                .mapToDouble(Pizza::getPrecio)
                .sum();
        return precioSinCombo - precioEspecial;
    }

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Pizza> getPizzas() { return pizzas; }
    public void setPizzas(List<Pizza> pizzas) { this.pizzas = pizzas; }

    public double getPrecioEspecial() { return precioEspecial; }
    public void setPrecioEspecial(double precioEspecial) { this.precioEspecial = precioEspecial; }

    @Override
    public String toString() {
        return String.format("Combo{id=%d, nombre='%s', pizzas=%d, precio=%.2f}",
                id, nombre, pizzas.size(), precioEspecial);
    }
}