package com.calendariodp.controller.usuario;

import com.calendariodp.domain.usuario.Usuario;
import com.calendariodp.services.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public record RegistroRequest(
            @NotEmpty String nombre,
            @NotEmpty @Email String correo,
            @NotEmpty @Size(min = 6) String contrasena,
            @NotEmpty String username
    ) {}

    @PostMapping("/registrar")
    public Mono<ResponseEntity<Usuario>> registrar(@RequestBody RegistroRequest registroRequest) {
        return usuarioService.registrarUsuario(
                        registroRequest.nombre(),
                        registroRequest.correo(),
                        registroRequest.contrasena(),
                        registroRequest.username()
                ).map(usuarioRegistrado -> ResponseEntity.status(HttpStatus.CREATED).body(usuarioRegistrado))
                .onErrorResume(UsuarioExistenteException.class, e -> ResponseEntity.status(HttpStatus.CONFLICT).body(null));
    }
}