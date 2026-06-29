package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CarritoTest {

    @Test
    void crearCarrito_conUsuarioProductoYCantidad_asignaPropiedades() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Producto producto = new Producto();
        producto.setId(2L);

        Carrito carrito = new Carrito();
        carrito.setId(3L);
        carrito.setUsuario(usuario);
        carrito.setProducto(producto);
        carrito.setCantidad(4);

        assertEquals(3L, carrito.getId());
        assertSame(usuario, carrito.getUsuario());
        assertSame(producto, carrito.getProducto());
        assertEquals(4, carrito.getCantidad());
    }
}
