package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Carrito;
import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Item;
import com.Panaderia.Servicios.ClientesServicio;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("carrito")
public class ControladorCarritoCompras {
    
    @Autowired
    private ClientesServicio clientesServicio;

    @ModelAttribute("carrito")
    public Carrito carrito() {
        return new Carrito();
    }

    @GetMapping("/agregarCarrito")
    public String agregarAlCarrito(@RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam double precio,
            @ModelAttribute("carrito") Carrito carrito,
            HttpServletRequest request) {

        carrito.agregarItem(new Item(id, nombre, descripcion, precio, 1));
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/eliminarDelCarrito")
    public String eliminarDelCarrito(@RequestParam Long id,
            @ModelAttribute("carrito") Carrito carrito) {
        carrito.eliminarItem(id);
        return "redirect:/carritocompras";
    }

    @PostMapping("/actualizarCantidad")
    public String actualizarCantidad(@RequestParam("id") Long id,
            @RequestParam("accion") String accion,
            @ModelAttribute("carrito") Carrito carrito) {
        carrito.actualizarCantidad(id, accion);
        return "redirect:/carritocompras";
    }

    @GetMapping("/carritocompras")
    public String verCarrito(@ModelAttribute("carrito") Carrito carrito, Model model) {
        agregarNombreClienteAlModelo(model);
        model.addAttribute("items", carrito.getItems());
        model.addAttribute("total", carrito.getTotal());
        return "CarritoCompras";
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
