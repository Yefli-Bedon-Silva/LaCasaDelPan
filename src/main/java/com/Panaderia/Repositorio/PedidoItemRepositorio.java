package com.Panaderia.Repositorio;

import com.Panaderia.Modelo.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PedidoItemRepositorio extends JpaRepository<PedidoItem, Long> {
    
}
