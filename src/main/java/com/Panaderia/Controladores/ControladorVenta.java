package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Carrito;
import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Servicios.ClientesServicio;
/*import com.Panaderia.Modelo.PedidoForm;*/
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorVenta {

    @Autowired
    private ClientesServicio clientesServicio;

    @GetMapping("/cuestionario")
    public String mostrarFormularioCompra(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String correo = auth.getName();
            clientesServicio.findClienteByCorreo(correo).ifPresent(cliente -> 
                model.addAttribute("nombreCliente", cliente.getNombreCli())
            );
        } else {
            model.addAttribute("nombreCliente", "Invitado");
        }

        Carrito carrito = (Carrito) session.getAttribute("carrito");
        if (carrito != null) {
            model.addAttribute("carrito", carrito.getItems());
            model.addAttribute("total", carrito.getTotal());
        } else {
            model.addAttribute("carrito", null);
            model.addAttribute("total", 0.0);
        }

        return "Cuestionario";
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
