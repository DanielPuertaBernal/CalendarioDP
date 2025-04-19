package com.calendariodp.services.usuario;

import com.calendariodp.domain.usuario.Usuario;
import com.calendariodp.crosscutting.exceptions.UsuarioExistenteException;
import com.calendariodp.repositories.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Usuario> registrarUsuario(String nombre, String correo, String contrasena, String username) {
        return usuarioRepository.existsByCorreo(correo)
                .flatMap(existeCorreo -> {
                    if (existeCorreo) {
                        return Mono.error(new UsuarioExistenteException("El correo electrónico ya está registrado."));
                    }
                    return usuarioRepository.existsByUsername(username);
                })
                .flatMap(existeUsername -> {
                    if (existeUsername) {
                        return Mono.error(new UsuarioExistenteException("El nombre de usuario ya existe."));
                    }
                    String contrasenaHashed = passwordEncoder.encode(contrasena);
                    Usuario nuevoUsuario = new Usuario(nombre, correo, contrasenaHashed, username);
                    return usuarioRepository.save(nuevoUsuario);
                });
    }

    public Mono<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}