package com.minimarket.service.impl;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @Test
    void findAll_devuelveProductosDelRepositorio() {
        Producto producto = new Producto();
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findAll();

        assertEquals(1, resultado.size());
        assertSame(producto, resultado.get(0));
    }

    @Test
    void findById_cuandoExiste_devuelveProducto() {
        Producto producto = new Producto();
        producto.setId(1L);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1L);

        assertSame(producto, resultado);
    }

    @Test
    void findById_cuandoNoExiste_devuelveNull() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Producto resultado = productoService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void save_delegaGuardadoEnRepositorio() {
        Producto producto = new Producto();
        producto.setNombre("Arroz");
        when(productoRepository.save(producto)).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertSame(producto, resultado);
        verify(productoRepository).save(producto);
    }

    @Test
    void deleteById_delegaEliminacionEnRepositorio() {
        productoService.deleteById(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void findByCategoriaId_devuelveProductosFiltrados() {
        Producto producto = new Producto();
        when(productoRepository.findByCategoriaId(2L)).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findByCategoriaId(2L);

        assertEquals(1, resultado.size());
        assertSame(producto, resultado.get(0));
    }
}
