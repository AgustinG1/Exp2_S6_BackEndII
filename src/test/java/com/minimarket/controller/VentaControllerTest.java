package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Venta;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.VentaService;
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

@WebMvcTest(VentaController.class)
@Import({SecurityConfig.class, com.minimarket.security.config.JwtRequestFilter.class})
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaService ventaService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private com.minimarket.security.util.JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    // ESCENARIO: Cajero genera venta (Permitido)
    @Test
    @WithMockUser(username = "cajero", roles = {"CAJERO"})
    void cuandoCajeroGeneraVenta_entoncesDevuelve200() throws Exception {
        Venta venta = new Venta(); 
        Mockito.when(ventaService.save(Mockito.any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/api/ventas").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isOk());
    }

    // ESCENARIO: Cliente intenta generar venta (Prohibido)
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void cuandoClienteIntentaGenerarVenta_entoncesDevuelve403() throws Exception {
        Venta venta = new Venta();
        mockMvc.perform(post("/api/ventas").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isForbidden());
    }

    // ESCENARIO DE NEGOCIO: Intentar venta sin stock (Debe fallar)
    @Test
    @WithMockUser(username = "cajero", roles = {"CAJERO"})
    void cuandoCajeroGeneraVentaSinStock_entoncesDevuelve500() throws Exception {
        Venta venta = new Venta();
        // Simulamos que el servicio lanza una excepción porque no hay stock
        Mockito.when(ventaService.save(Mockito.any(Venta.class)))
               .thenThrow(new RuntimeException("Stock insuficiente"));

        mockMvc.perform(post("/api/ventas").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venta)))
                .andExpect(status().isBadRequest());
    }
}
