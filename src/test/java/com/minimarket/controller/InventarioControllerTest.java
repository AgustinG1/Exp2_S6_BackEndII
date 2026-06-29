package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Inventario;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventarioController.class)
@Import({SecurityConfig.class, com.minimarket.security.config.JwtRequestFilter.class}) // Importamos tu configuración de seguridad real
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService; // Necesario para que SecurityConfig no falle

    @MockBean
    private com.minimarket.security.util.JwtUtil jwtUtil; // Dependencia para JwtRequestFilter

    @Autowired
    private ObjectMapper objectMapper;

    private Inventario movimientoMock;

    @BeforeEach
    void setUp() {
        movimientoMock = new Inventario();
        movimientoMock.setId(1L);
        // movimientoMock.setTipoMovimiento("ENTRADA");
        // movimientoMock.setCantidad(50);
    }
    
    // ESCENARIO DE ÉXITO: Administrador registra movimiento
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cuandoAdminRegistraMovimiento_entoncesDevuelve200() throws Exception {
        Mockito.when(inventarioService.save(Mockito.any(Inventario.class))).thenReturn(movimientoMock);

        // Suponiendo endpoint para registrar es un POST a /api/inventario
        mockMvc.perform(post("/api/inventario").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimientoMock)))
                .andExpect(status().isOk()); // o isCreated() dependiendo de controller
    }
    
    // ESCENARIO DE ERROR: Cajero/Cliente intenta registrar movimiento
    @Test
    @WithMockUser(username = "cajero", roles = {"CAJERO"})
    void cuandoCajeroRegistraMovimiento_entoncesDevuelve403() throws Exception {
        mockMvc.perform(post("/api/inventario").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimientoMock)))
                .andExpect(status().isForbidden());
    }
}
