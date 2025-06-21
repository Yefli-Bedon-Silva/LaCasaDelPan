package com.Panaderia.Repositorio;

import com.Panaderia.Modelo.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long>{
   List<Pedido> findByCliente_IdCli(Integer idCli);


}
