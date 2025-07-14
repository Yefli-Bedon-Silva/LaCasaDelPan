package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Carrito;
import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Item;
import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Modelo.PedidoItem;
import com.Panaderia.Modelo.Producto;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.dao.ProductoRepositorio;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorPedido {

    @Autowired
    private PedidoRepositorio pedidoRepository;

    @Autowired
    private ProductoRepositorio productoRepository;

    @Autowired
    private ClientesServicio clientesServicio;

    @PostMapping("/pedido")
    public String procesarPedido(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute("carrito");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }

        String correo = auth.getName();
        Clientes cliente = clientesServicio.findClienteByCorreo(correo).orElse(null);

        if (carrito == null || carrito.getItems().isEmpty() || cliente == null) {
            return "redirect:/carritocompras";
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("pendiente");

        List<PedidoItem> items = new ArrayList<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (Item item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getId()).orElseThrow();
            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setPedido(pedido);
            pedidoItem.setIdProducto(producto);
            pedidoItem.setCantidad(item.getCantidad());
            pedidoItem.setPrecioUnitario(BigDecimal.valueOf(producto.getPrecio()));
            BigDecimal totalItem = BigDecimal.valueOf(producto.getPrecio())
                    .multiply(BigDecimal.valueOf(item.getCantidad()));
            pedidoItem.setTotal(totalItem);
            items.add(pedidoItem);
            totalPedido = totalPedido.add(totalItem);
        }

        pedido.setItems(items);
        pedido.setTotal(totalPedido);
        pedidoRepository.save(pedido);

        // Guardar el pedido en sesión
        session.setAttribute("ultimoPedido", pedido);

        // Vaciar carrito
        carrito.setItems(new ArrayList<>());
        session.setAttribute("carrito", carrito);

        return "redirect:/resumen"; // redirige a la vista final
    }

@GetMapping("/resumen")
public String mostrarResumenPedido(HttpSession session, Model model) {
    Pedido pedido = (Pedido) session.getAttribute("ultimoPedido");

    if (pedido == null) {
        return "redirect:/listapanes";
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()
            && !"anonymousUser".equals(authentication.getName())) {

        String correo = authentication.getName();
        Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);

        if (clienteOpt.isPresent()) {
            Clientes cliente = clienteOpt.get();
            model.addAttribute("nombreCliente", cliente.getNombreCli()); // ✅ Solo nombre
        } else {
            model.addAttribute("nombreCliente", "Invitado");
        }
    } else {
        model.addAttribute("nombreCliente", "Invitado");
    }

    model.addAttribute("pedido", pedido);
    session.removeAttribute("ultimoPedido");
    return "Pedido";
}
}
