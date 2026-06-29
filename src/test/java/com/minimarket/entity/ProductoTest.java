package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ProductoTest {

    @Test
    void crearProducto_conDatosValidos_asignaPropiedades() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Abarrotes");

        Producto producto = new Producto();
        producto.setId(10L);
        producto.setNombre("Arroz");
        producto.setPrecio(1500.0);
        producto.setStock(20);
        producto.setCategoria(categoria);

        assertEquals(10L, producto.getId());
        assertEquals("Arroz", producto.getNombre());
        assertEquals(1500.0, producto.getPrecio());
        assertEquals(20, producto.getStock());
        assertSame(categoria, producto.getCategoria());
    }

    @Test
    void actualizarStock_reflejaNuevoValor() {
        Producto producto = new Producto();
        producto.setStock(5);

        producto.setStock(12);

        assertEquals(12, producto.getStock());
    }
}
