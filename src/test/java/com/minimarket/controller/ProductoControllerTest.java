package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Producto;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.service.ProductoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import({SecurityConfig.class, com.minimarket.security.config.JwtRequestFilter.class}) // Necesario para cargar nuestras reglas de seguridad y @EnableMethodSecurity
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService; // Dependencia que necesita SecurityConfig

    @MockBean
    private com.minimarket.security.util.JwtUtil jwtUtil; // Dependencia para JwtRequestFilter

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoMock;

    @BeforeEach
    void setUp() {
        productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Arroz");
        productoMock.setPrecio(1500.0);
        productoMock.setStock(20);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cuandoAdminCreaProducto_entoncesDevuelve200() throws Exception {
        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenReturn(productoMock);

        mockMvc.perform(post("/api/productos").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void cuandoClienteCreaProducto_entoncesDevuelve403() throws Exception {
        mockMvc.perform(post("/api/productos").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cuandoProductoNoExiste_alConsultarPorId_devuelve404() throws Exception {
        Mockito.when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    // Admin modificando producto
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cuandoUsuarioEsAdmin_entoncesPuedeModificarProducto_devuelve200() throws Exception {
        Mockito.when(productoService.findById(1L)).thenReturn(productoMock);
        Mockito.when(productoService.save(Mockito.any(Producto.class))).thenReturn(productoMock);

        mockMvc.perform(put("/api/productos/1").with(csrf()) // Añadimos CSRF al mock
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isOk()); 
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cuandoAdminModificaProductoInexistente_entoncesDevuelve404() throws Exception {
        Mockito.when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/productos/99").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isNotFound());
    }

    // ESCENARIO DE ERROR Cliente intentando modificar
    @Test
    @WithMockUser(username = "cliente", roles = {"CLIENTE"})
    void cuandoUsuarioEsCliente_entoncesNoPuedeModificarProducto_devuelve403() throws Exception {
        mockMvc.perform(put("/api/productos/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isForbidden()); 
    }

    // 🔴 ESCENARIO DE ERROR: Usuario no autenticado (sin token/sesión)
    @Test
    void cuandoUsuarioNoEstaAutenticado_entoncesBloqueaAcceso_devuelve401() throws Exception {
        mockMvc.perform(put("/api/productos/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isUnauthorized()); 
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cuandoAdminEliminaProductoInexistente_entoncesDevuelve404() throws Exception {
        Mockito.when(productoService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/productos/99").with(csrf()))
                .andExpect(status().isNotFound());
    }
}
