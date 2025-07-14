package com.Panaderia.dto;

import jakarta.validation.constraints.*;

public class LoginDTO {

    @NotEmpty(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    private String correo;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String contraseña;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

   
}