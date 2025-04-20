package com.calendariodp.controller.usuario;

import com.calendariodp.crosscutting.exceptions.UsuarioExistenteException;
import com.calendariodp.crosscutting.exceptions.ValidacionException;
import com.calendariodp.domain.usuario.Usuario;
import com.calendariodp.services.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/usuarios")
public class RegistroController {
    private static final String REGISTRO_EXITOSO = "Usuario registrado exitosamente";
    private static final String ERROR_INESPERADO = "Error inesperado: ";

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public record RegistroRequest(String nombre, String correo, String contrasena, String username) {}
    public record RegistroResponse(String mensaje, Usuario usuario) {}
    public record ErrorResponse(String mensaje) {}

    @PostMapping("/registrar")
    public Mono<ResponseEntity<Object>> registrar(@RequestBody RegistroRequest req) {
        return usuarioService.registrarUsuario(
                        req.nombre(),
                        req.correo(),
                        req.contrasena(),
                        req.username()
                )
                .map(this::crearRespuestaExitosa)
                .onErrorResume(UsuarioExistenteException.class, this::manejarErrorUsuarioExistente)
                .onErrorResume(ValidacionException.class, this::manejarErrorValidacion)
                .onErrorResume(Exception.class, this::manejarErrorGeneral);
    }

    private ResponseEntity<Object> crearRespuestaExitosa(Usuario usuario) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegistroResponse(REGISTRO_EXITOSO, usuario));
    }

    private Mono<ResponseEntity<Object>> manejarErrorUsuarioExistente(UsuarioExistenteException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getMessage())));
    }

    private Mono<ResponseEntity<Object>> manejarErrorValidacion(ValidacionException e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage())));
    }

    private Mono<ResponseEntity<Object>> manejarErrorGeneral(Exception e) {
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ERROR_INESPERADO + e.getMessage())));
    }
}