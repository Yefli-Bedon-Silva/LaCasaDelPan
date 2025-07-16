package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.Servicios.PedidoServicio;
import com.Panaderia.Servicios.ProductoServicio;
import com.Panaderia.Servicios.ReclamoServicio;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorAdmin {

    private final ClientesServicio clientesServicio;
    private final ProductoServicio productoServicio;
    private final ReclamoServicio reclamoServicio;
    private final PedidoServicio pedidoServicio;

    public ControladorAdmin(ClientesServicio clientesServicio,
            ProductoServicio productoServicio,
            ReclamoServicio reclamoServicio,
            PedidoServicio pedidoServicio) {      
        this.clientesServicio = clientesServicio;
        this.productoServicio = productoServicio;
        this.reclamoServicio = reclamoServicio;
        this.pedidoServicio = pedidoServicio;
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

        model.addAttribute("totalUsuarios", clientesServicio.contarClientes());
        model.addAttribute("totalProductos", productoServicio.contarProductos());
        model.addAttribute("totalReclamos", reclamoServicio.contarReclamos());
        model.addAttribute("pedidosPendientes", pedidoServicio.contarPedidosPendientes());

        return "admin";
    }
}