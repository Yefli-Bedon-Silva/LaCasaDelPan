package com.Panaderia.Controladores;

import com.Panaderia.Servicios.ClientesServicio;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorAdminClientes {

    private final ClientesServicio clientesServicio;

    public ControladorAdminClientes(ClientesServicio clientesServicio) {
        this.clientesServicio = clientesServicio;
    }

    @GetMapping("/adminclientes")
    public String adminClientes(Model model) {
        agregarNombreUsuarioAlModelo(model);
        return "Adminclientes";
    }

    private void agregarNombreUsuarioAlModelo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String correo = auth.getName();
            clientesServicio.findClienteByCorreo(correo).ifPresentOrElse(
                    cliente -> model.addAttribute("nombreUsuario", cliente.getNombreCli()),
                    () -> model.addAttribute("nombreUsuario", correo)
            );
        } else {
            model.addAttribute("nombreUsuario", "Invitado");
        }
    }
}
