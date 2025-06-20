package com.Panaderia.Modelo;

import jakarta.persistence.*;  
import java.sql.Timestamp;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reclamo")
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reclamo")  
    private Integer idReclamo;
    
    @ManyToOne
    @JoinColumn(name = "id_cli")
    private Clientes idCliente;
    
    @Column(name = "fecha_pedido")  
    private String fechapedido;
    
    @Column(name = "motivo_reclamo")  
    private String motivoReclamo;
    
    @Column(name = "detalle")  
    private String detalle;
    
    @Column(name = "estado")  
    private String estadoReclamo;
    
    @Column(name = "fecha_reclamo")
    private Timestamp fechaReclamo;
}
