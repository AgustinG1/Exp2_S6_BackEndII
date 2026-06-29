package com.minimarket.service.impl;

import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
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
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @Test
    void findAll_devuelveVentasDelRepositorio() {
        Venta venta = new Venta();
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.findAll();

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
    }

    @Test
    void findById_cuandoExiste_devuelveVenta() {
        Venta venta = new Venta();
        venta.setId(1L);
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.findById(1L);

        assertSame(venta, resultado);
    }

    @Test
    void findById_cuandoNoExiste_devuelveNull() {
        when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

        Venta resultado = ventaService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void save_delegaGuardadoEnRepositorio() {
        Venta venta = new Venta();
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta resultado = ventaService.save(venta);

        assertSame(venta, resultado);
        verify(ventaRepository).save(venta);
    }

    @Test
    void findByUsuarioId_devuelveVentasDelUsuario() {
        Venta venta = new Venta();
        when(ventaRepository.findByUsuarioId(3L)).thenReturn(List.of(venta));

        List<Venta> resultado = ventaService.findByUsuarioId(3L);

        assertEquals(1, resultado.size());
        assertSame(venta, resultado.get(0));
    }
}
