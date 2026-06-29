package com.minimarket;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                      ProductoRepository productoRepository, CategoriaRepository categoriaRepository,
                      PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> rolRepository.save(new Rol("ROLE_ADMIN")));
        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                .orElseGet(() -> rolRepository.save(new Rol("ROLE_CLIENTE")));

        usuarioRepository.findByUsername("admin")
                .orElseGet(() -> crearUsuario("admin", "12345", rolAdmin));
        usuarioRepository.findByUsername("cliente")
                .orElseGet(() -> crearUsuario("cliente", "12345", rolCliente));

        if (categoriaRepository.count() == 0 && productoRepository.count() == 0) {
            Categoria categoria = new Categoria();
            categoria.setNombre("Lacteos");
            categoria = categoriaRepository.save(categoria);

            Producto producto = new Producto();
            producto.setNombre("Leche");
            producto.setPrecio(1000.0);
            producto.setStock(10);
            producto.setCategoria(categoria);
            productoRepository.save(producto);
        }
    }

    private Usuario crearUsuario(String username, String password, Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRoles(Set.of(rol));
        return usuarioRepository.save(usuario);
    }
}
