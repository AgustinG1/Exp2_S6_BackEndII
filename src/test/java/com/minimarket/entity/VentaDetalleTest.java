package com.minimarket.entity;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class VentaDetalleTest {

    @Test
    void crearVenta_conUsuarioFechaYDetalles_asignaRelacion() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Date fecha = new Date();
        Venta venta = new Venta();
        venta.setId(10L);
        venta.setUsuario(usuario);
        venta.setFecha(fecha);

        Producto producto = new Producto();
        producto.setId(2L);
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(20L);
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(3);
        detalle.setPrecio(1200.0);
        venta.setDetalles(List.of(detalle));

        assertEquals(10L, venta.getId());
        assertSame(usuario, venta.getUsuario());
        assertSame(fecha, venta.getFecha());
        assertEquals(1, venta.getDetalles().size());
        assertSame(detalle, venta.getDetalles().get(0));
        assertSame(venta, detalle.getVenta());
        assertSame(producto, detalle.getProducto());
        assertEquals(3, detalle.getCantidad());
        assertEquals(1200.0, detalle.getPrecio());
    }
}
