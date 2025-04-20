package com.calendariodp.repositories.usuario;

import com.calendariodp.domain.usuario.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, UUID> {
    Mono<Boolean> existsByCorreo(String correo);
    Mono<Boolean> existsByUsername(String username);
    Mono<Usuario> findByUsername(String username);
    Flux<Usuario> findAll();
}