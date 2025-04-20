package com.calendariodp.controller.login;

import com.calendariodp.controller.login.LoginRequest.LoginRequest;
import com.calendariodp.controller.login.LoginResponse.LoginResponse;
import com.calendariodp.security.JwtUtil;
import com.calendariodp.services.usuario.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return usuarioService.obtenerUsuarioPorUsername(loginRequest.username())
                .filter(usuario -> passwordEncoder.matches(loginRequest.password(), usuario.getContrasena()))
                .map(usuario -> {
                    String token = jwtUtil.generateToken(usuario.getUsername());
                    return ResponseEntity.ok(new LoginResponse(token));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}