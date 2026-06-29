package com.minimarket.service.impl;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
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
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    @Test
    void findAll_devuelveCategoriasDelRepositorio() {
        Categoria categoria = new Categoria();
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.findAll();

        assertEquals(1, resultado.size());
        assertSame(categoria, resultado.get(0));
    }

    @Test
    void findById_cuandoExiste_devuelveCategoria() {
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.findById(1L);

        assertSame(categoria, resultado);
    }

    @Test
    void findById_cuandoNoExiste_devuelveNull() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        Categoria resultado = categoriaService.findById(99L);

        assertNull(resultado);
    }

    @Test
    void save_delegaGuardadoEnRepositorio() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Lacteos");
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.save(categoria);

        assertSame(categoria, resultado);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void deleteById_delegaEliminacionEnRepositorio() {
        categoriaService.deleteById(1L);

        verify(categoriaRepository).deleteById(1L);
    }
}
