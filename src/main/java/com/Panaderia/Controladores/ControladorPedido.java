package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Carrito;
import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Item;
import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Modelo.PedidoItem;
import com.Panaderia.Modelo.Producto;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.dao.ProductoRepositorio;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorPedido {
    @Autowired
    private PedidoRepositorio pedidoRepository;

    @Autowired
    private ProductoRepositorio productoRepository;
    
    
@PostMapping("/pedido")
public String procesarPedido(HttpSession session, Model model) {
    Carrito carrito = (Carrito) session.getAttribute("carrito");
    Clientes cliente = (Clientes) session.getAttribute("cliente");

    if (carrito == null || carrito.getItems().isEmpty() || cliente == null) {
        return "redirect:/carritocompras";
    }

    // Crear el pedido
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
        

        items.add(pedidoItem);
        totalPedido = totalPedido.add(totalItem);
    }

    pedido.setItems(items);
    

    pedidoRepository.save(pedido);

    // Agregar datos al modelo para el resumen
    model.addAttribute("items", carrito.getItems());
    model.addAttribute("total", carrito.getTotal());

    // Vaciar el carrito
    carrito.setItems(new ArrayList<>());
    session.setAttribute("carrito", carrito);

    return "Pedido"; // muestra la vista resumen o éxito
}

}
