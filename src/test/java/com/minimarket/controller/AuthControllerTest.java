package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.service.CustomUserDetailsService;
import com.minimarket.security.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, com.minimarket.security.config.JwtRequestFilter.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Dependencias para que SecurityConfig cargue bien
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    // Dependencias del AuthController y JwtRequestFilter
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cuandoLoginExitoso_entoncesDevuelveToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("12345");

        Authentication authMock = Mockito.mock(Authentication.class);
        Mockito.when(authMock.getName()).thenReturn("admin");
        
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);
                
        Mockito.when(jwtUtil.generateToken("admin")).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));
    }

    @Test
    void cuandoLoginFalla_entoncesDevuelve401() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrongpassword");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        mockMvc.perform(post("/api/auth/login").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
