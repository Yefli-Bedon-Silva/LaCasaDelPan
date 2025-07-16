package com.Panaderia.Repositorio;

import com.Panaderia.Modelo.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoItemRepositorio extends JpaRepository<PedidoItem, Long> {

    @Query("SELECT COUNT(pi) > 0 FROM PedidoItem pi WHERE pi.idProducto.id_prod = :idProd")
    boolean existsByProductoId(@Param("idProd") Long idProd);
}