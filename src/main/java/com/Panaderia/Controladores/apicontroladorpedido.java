package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.Servicios.PedidoServicio;
import com.Panaderia.dto.ClientesDTO;
import com.Panaderia.dto.PedidoDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adminventas")
public class apicontroladorpedido {

    @Autowired
    private PedidoRepositorio pedidoRepository;
      @Autowired
    private PedidoServicio pedidoServicio;

    @GetMapping("/listar")
    public List<PedidoDTO> listarPedidos() {
        return pedidoServicio.listarPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        PedidoDTO pedido = pedidoServicio.listarPedidos().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        return ResponseEntity.ok(pedido);
    }


     @PostMapping("/editar/{id}")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        pedidoServicio.actualizarEstadoPedido(id, estado);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable Long id) {
        pedidoServicio.eliminarPedido(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/mis-pedidos")
public ResponseEntity<List<PedidoDTO>> listarMisPedidos(HttpSession session) {
    Clientes cliente = (Clientes) session.getAttribute("cliente");
    if (cliente == null) {
        return ResponseEntity.status(401).build(); // no autorizado
    }
    Integer idCliente = cliente.getIdCli();
    List<PedidoDTO> pedidos = pedidoServicio.listarPedidosPorCliente(idCliente);
    return ResponseEntity.ok(pedidos);
}
}