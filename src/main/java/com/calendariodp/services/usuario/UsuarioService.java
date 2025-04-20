package com.calendariodp.services.usuario;

import com.calendariodp.crosscutting.exceptions.UsuarioExistenteException;
import com.calendariodp.crosscutting.exceptions.ValidacionException; // Crea esta excepción
import com.calendariodp.crosscutting.utils.UtilText;
import com.calendariodp.domain.usuario.Usuario;
import com.calendariodp.repositories.usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
        // Validaciones manuales
        if (UtilText.isNullOrEmpty(nombre)) {
            return Mono.error(new ValidacionException("El nombre no puede estar vacío."));
        }
        if (UtilText.isNullOrEmpty(correo) || !UtilText.emailStringIsValid(correo)) {
            return Mono.error(new ValidacionException("El correo electrónico no es válido."));
        }
        if (UtilText.isNullOrEmpty(contrasena) || !UtilText.isPasswordValid(contrasena)) {
            return Mono.error(new ValidacionException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial."));
        }
        if (UtilText.isNullOrEmpty(username)) {
            return Mono.error(new ValidacionException("El nombre de usuario no puede estar vacío."));
        }

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

    public Flux<Usuario> obtenerTodosUsuarios() {
        return usuarioRepository.findAll();
    }
}