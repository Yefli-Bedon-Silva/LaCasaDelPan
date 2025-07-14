package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Servicios.ClientesServicio;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class ControladorClientePerfil {

    private final ClientesServicio clientesServicio;

    public ControladorClientePerfil(ClientesServicio clientesServicio) {
        this.clientesServicio = clientesServicio;
    }

    @GetMapping("/cliente/perfil")
    public String mostrarPerfilCliente(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            // Usuario no autenticado, redirigir a login
            return "redirect:/login";
        }

        String correo = auth.getName();
        Clientes cliente = clientesServicio.findClienteByCorreo(correo).orElse(null);
        if (cliente == null) {
            // Opcional: redirigir o mostrar error si no se encuentra el cliente
            return "redirect:/login?error=clienteNoEncontrado";
        }

        model.addAttribute("cliente", cliente);

        // Agregar nombreCliente al modelo para mostrar en header o donde se use
        agregarNombreClienteAlModelo(model);

        return "clientePerfil";  // Nombre de la plantilla Thymeleaf
    }

    private void agregarNombreClienteAlModelo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            String correo = authentication.getName();
            Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);

            if (clienteOpt.isPresent()) {
                model.addAttribute("nombreCliente", clienteOpt.get().getNombreCli());
                return;
            }
        }
        model.addAttribute("nombreCliente", "Invitado");
    }
}