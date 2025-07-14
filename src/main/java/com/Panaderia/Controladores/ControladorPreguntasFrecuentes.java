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
public class ControladorPreguntasFrecuentes {

    @Autowired
    private ClientesServicio clientesServicio;

    @GetMapping("/preguntasfrecuentes")
    public String preguntasFrecuentes(Model modelo) {
        agregarNombreClienteAlModelo(modelo);
        return "FrmPreguntasFrecuentes";
    }

    private void agregarNombreClienteAlModelo(Model modelo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            String correo = authentication.getName();
            Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);

            if (clienteOpt.isPresent()) {
                modelo.addAttribute("nombreCliente", clienteOpt.get().getNombreCli());
                return;
            }
        }
        modelo.addAttribute("nombreCliente", "Invitado");
    }
}
