package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class InventarioTest {

    @Test
    void crearMovimientoInventario_conDatosValidos_asignaPropiedades() {
        Producto producto = new Producto();
        producto.setId(1L);
        Date fecha = new Date();

        Inventario inventario = new Inventario();
        inventario.setId(5L);
        inventario.setProducto(producto);
        inventario.setCantidad(30);
        inventario.setTipoMovimiento("ENTRADA");
        inventario.setFechaMovimiento(fecha);

        assertEquals(5L, inventario.getId());
        assertSame(producto, inventario.getProducto());
        assertEquals(30, inventario.getCantidad());
        assertEquals("ENTRADA", inventario.getTipoMovimiento());
        assertSame(fecha, inventario.getFechaMovimiento());
    }
}
