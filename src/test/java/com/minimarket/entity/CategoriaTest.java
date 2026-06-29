package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CategoriaTest {

    @Test
    void crearCategoria_conProductos_asignaRelacion() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Lacteos");

        Producto producto = new Producto();
        producto.setId(2L);
        producto.setCategoria(categoria);
        categoria.setProductos(List.of(producto));

        assertEquals(1L, categoria.getId());
        assertEquals("Lacteos", categoria.getNombre());
        assertEquals(1, categoria.getProductos().size());
        assertSame(producto, categoria.getProductos().get(0));
        assertSame(categoria, producto.getCategoria());
    }
}
