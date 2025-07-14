package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Servicios.ClientesServicio;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorAdmin {

    private final ClientesServicio clientesServicio;

    public ControladorAdmin(ClientesServicio clientesServicio) {
        this.clientesServicio = clientesServicio;
    }

    @GetMapping("/admin")
    public String adminHome(Model model, Authentication authentication) {
        String correo = authentication.getName();
        Clientes cliente = clientesServicio.findClienteByCorreo(correo).orElse(null);
        if (cliente != null) {
            model.addAttribute("nombreUsuario", cliente.getNombreCli());
        } else {
            model.addAttribute("nombreUsuario", correo);
        }

        // Datos 
        model.addAttribute("totalUsuarios", clientesServicio.contarClientes());
        model.addAttribute("totalProductos", 14); 
        model.addAttribute("ventasHoy", 12);      
        model.addAttribute("pedidosPendientes", 5);

        return "admin";
    }
}
