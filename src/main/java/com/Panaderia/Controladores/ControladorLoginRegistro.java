package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.dto.RegisterDto;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorLoginRegistro {

    @Autowired
    private ClientesServicio clientesServicio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Mostrar formulario de login
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginForm", new LoginDTO());
        return "FrmLogin";
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "FrmRegistro";
    }

    // Procesar registro
    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("registerDto") RegisterDto registerDto,
            BindingResult result, Model model) {

        if (!registerDto.getContraseña().equals(registerDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.registerDto", "Las contraseñas no coinciden");
        }

        if (clientesServicio.findClienteByCorreo(registerDto.getCorreo()).isPresent()) {
            result.rejectValue("correo", "error.registerDto", "El correo ya está registrado");
        }

        if (result.hasErrors()) {
            return "FrmRegistro";
        }

        Clientes cliente = new Clientes();
        cliente.setNombreCli(registerDto.getNombreCli());
        cliente.setApellidosCli(registerDto.getApellidosCli());
        cliente.setCorreo(registerDto.getCorreo());
        cliente.setDni(registerDto.getDni());
        cliente.setDireccion(registerDto.getDireccion());
        cliente.setTelefono(registerDto.getTelefono());

        // Encriptar contraseña
        cliente.setContraseña(passwordEncoder.encode(registerDto.getContraseña()));

        // Asignar rol por defecto (ROLE_USER)
        clientesServicio.asignarRolPorDefecto(cliente);

        clientesServicio.agregarCliente(cliente);

        model.addAttribute("success", true);
        model.addAttribute("registerDto", new RegisterDto()); // Limpiar formulario
        return "FrmRegistro";
    }
}
