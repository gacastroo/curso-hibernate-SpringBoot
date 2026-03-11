package com.curso.demospring;

import com.curso.demospring.controlador.ProductoController;
import com.curso.demospring.modelo.Producto;
import com.curso.demospring.servicio.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService servicio;

    @Test
    @DisplayName("Should return all products with 200 OK")
    void listarTodos_ok() throws Exception {
        // Arrange
        List<Producto> productos = List.of(
                new Producto(1L, "Margarita", 8.5, "CLASICA"),
                new Producto(2L, "Pepperoni", 9.9, "CARNE")
        );
        given(servicio.listarTodos()).willReturn(productos);

        // Act + Assert
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Margarita")))
                .andExpect(jsonPath("$[0].precio", is(closeTo(8.5, 0.001))))
                .andExpect(jsonPath("$[0].categoria", is("CLASICA")));
    }

    @Test
    @DisplayName("Should return product by id with 200 OK when exists")
    void buscarPorId_found_ok() throws Exception {
        // Arrange
        Producto producto = new Producto(5L, "Hawaiana", 10.0, "CLASICA");
        given(servicio.buscarPorId(5L)).willReturn(Optional.of(producto));

        // Act + Assert
        mockMvc.perform(get("/api/productos/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.nombre", is("Hawaiana")))
                .andExpect(jsonPath("$.precio", is(closeTo(10.0, 0.001))))
                .andExpect(jsonPath("$.categoria", is("CLASICA")));
    }

    @Test
    @DisplayName("Should return 404 Not Found when product id does not exist")
    void buscarPorId_notFound_404() throws Exception {
        // Arrange
        given(servicio.buscarPorId(404L)).willReturn(Optional.empty());

        // Act + Assert
        mockMvc.perform(get("/api/productos/{id}", 404))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update product with id from path and return 200 OK")
    void actualizar_setsIdFromPath_andSaves_ok() throws Exception {
        // Arrange
        long id = 9L;
        Producto existente = new Producto(id, "4 Quesos", 11.0, "CLASICA");
        given(servicio.buscarPorId(id)).willReturn(Optional.of(existente));
        // Echo back the entity passed to save, so we can assert id propagation
        given(servicio.guardar(any(Producto.class))).willAnswer(inv -> inv.getArgument(0));

        Producto body = new Producto(null, "4 Quesos Especial", 12.5, "GOURMET");

        // Act + Assert
        mockMvc.perform(put("/api/productos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is((int) id)))
                .andExpect(jsonPath("$.nombre", is("4 Quesos Especial")))
                .andExpect(jsonPath("$.precio", is(closeTo(12.5, 0.001))))
                .andExpect(jsonPath("$.categoria", is("GOURMET")));

        // Verify that the service was invoked with the id from the path
        verify(servicio).guardar(any(Producto.class));
    }

    @Test
    @DisplayName("Should delete product by id and return 204 No Content when exists")
    void eliminar_exists_noContent() throws Exception {
        // Arrange
        long id = 7L;
        given(servicio.buscarPorId(id)).willReturn(Optional.of(new Producto(id, "BBQ", 13.0, "CARNE")));
        doNothing().when(servicio).eliminar(eq(id));

        // Act + Assert
        mockMvc.perform(delete("/api/productos/{id}", id))
                .andExpect(status().isNoContent());

        verify(servicio).eliminar(id);
    }
}
