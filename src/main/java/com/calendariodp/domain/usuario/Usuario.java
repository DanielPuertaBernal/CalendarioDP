package com.calendariodp.domain.usuario;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("usuario")
public class Usuario {

    @Id
    @Column("id")
    private UUID id;

    @Column("nombre")
    private String nombre;

    @Column("correo")
    private String correo;

    @Column("contrasena")
    private String contrasena;

    @Column("usuario")
    private String username;

    public Usuario() {

    }

    public Usuario(String nombre, String correo, String contrasena, String username) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.username = username;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
