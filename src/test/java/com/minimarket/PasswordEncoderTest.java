package com.minimarket;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordEncoderTest {

    @Test
    void bcrypt_validaPasswordCorrectaYRechazaIncorrecta() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hash = encoder.encode("12345");

        assertTrue(encoder.matches("12345", hash));
        assertFalse(encoder.matches("incorrecta", hash));
    }
}
