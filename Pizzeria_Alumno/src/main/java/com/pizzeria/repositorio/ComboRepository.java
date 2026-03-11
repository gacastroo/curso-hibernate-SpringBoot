package com.pizzeria.repositorio;
import com.pizzeria.modelo.Combo;
import java.util.List;

public interface ComboRepository {

    Combo guardar(Combo combo);

    Combo buscarPorId(Long id);

    List<Combo> buscarTodos();

    void eliminar(Long id);
}