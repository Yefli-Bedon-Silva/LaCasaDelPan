package com.Panaderia.Servicios;

import com.Panaderia.Modelo.Pedido;
import com.Panaderia.Repositorio.PedidoRepositorio;
import com.Panaderia.dto.ClientesDTO;
import com.Panaderia.dto.PedidoDTO;
import com.Panaderia.dto.PedidoItemDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServicio {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    // Listar todos los pedidos
       public List<PedidoDTO> listarPedidos() {
        List<Pedido> pedidos = pedidoRepositorio.findAll();

        return pedidos.stream().map(pedido -> {
            PedidoDTO dto = new PedidoDTO();
            dto.setId(pedido.getId_pedido());
            dto.setCliente(new ClientesDTO(pedido.getCliente()));
            dto.setEstado(pedido.getEstado());
            dto.setFecha(pedido.getFecha());
           

            List<PedidoItemDTO> items = pedido.getItems().stream().map(item -> {
                PedidoItemDTO itemDTO = new PedidoItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setIdProducto(item.getIdProducto().getId_prod());
                itemDTO.setNombreProducto(item.getIdProducto().getNombre());
                itemDTO.setCantidad(item.getCantidad());
                itemDTO.setPrecioUnitario(item.getPrecioUnitario());
                
                return itemDTO;
            }).collect(Collectors.toList());

            dto.setItems(items);
            return dto;
        }).collect(Collectors.toList());
    }

    // Obtener un pedido por ID
    public Pedido obtenerPedidoPorId(Long id) {
        return pedidoRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado con ID: " + id));
    }

    // Editar estado del pedido (puedes expandir esto para editar más cosas)
    public Pedido actualizarEstadoPedido(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPedidoPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepositorio.save(pedido);
    }

    // Eliminar un pedido por ID
    public void eliminarPedido(Long id) {
        if (!pedidoRepositorio.existsById(id)) {
            throw new EntityNotFoundException("Pedido no encontrado con ID: " + id);
        }
        pedidoRepositorio.deleteById(id);
    }
    
    
    
    public List<PedidoDTO> listarPedidosPorCliente(Integer idCli) {
    List<Pedido> pedidos = pedidoRepositorio.findByCliente_IdCli(idCli);

    return pedidos.stream().map(pedido -> {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId_pedido());
        dto.setCliente(new ClientesDTO(pedido.getCliente()));
        dto.setEstado(pedido.getEstado());
        dto.setFecha(pedido.getFecha());
       
        List<PedidoItemDTO> items = pedido.getItems().stream().map(item -> {
            PedidoItemDTO itemDTO = new PedidoItemDTO();
            itemDTO.setId(item.getId());
            itemDTO.setIdProducto(item.getIdProducto().getId_prod());
            itemDTO.setNombreProducto(item.getIdProducto().getNombre());
            itemDTO.setCantidad(item.getCantidad());
            itemDTO.setPrecioUnitario(item.getPrecioUnitario());
            
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setItems(items);
        return dto;
    }).collect(Collectors.toList());
}
}