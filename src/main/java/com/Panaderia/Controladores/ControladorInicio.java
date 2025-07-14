package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Servicios.ClientesServicio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorInicio {

    @Autowired
    private ClientesServicio clientesServicio;

    @GetMapping("/inicio") 
    public String mostrarInicio(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String correo = authentication.getName();
            Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);
            if (clienteOpt.isPresent()) {
                model.addAttribute("nombreCliente", clienteOpt.get().getNombreCli());
            } else {
                model.addAttribute("nombreCliente", "Invitado");
            }
        } else {
            model.addAttribute("nombreCliente", "Invitado");
        }
        return "inicio";  // o el nombre de la plantilla Thymeleaf
    }

}
