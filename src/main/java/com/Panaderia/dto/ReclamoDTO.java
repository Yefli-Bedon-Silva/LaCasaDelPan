package com.Panaderia.dto;

import com.Panaderia.Modelo.Clientes;
import java.sql.Timestamp;

public class ReclamoDTO {

    private Integer idReclamo;
    private Clientes idCliente;
    private String fechapedido;
    private String motivoReclamo;
    private String detalle;
    private String estadoReclamo;
    private Timestamp fechaReclamo;

    public ReclamoDTO(Integer idReclamo, Clientes idCliente, String fechapedido, String motivoReclamo, String detalle, String estadoReclamo, Timestamp fechaReclamo) {
        this.idReclamo = idReclamo;
        this.idCliente = idCliente;
        this.fechapedido = fechapedido;
        this.motivoReclamo = motivoReclamo;
        this.detalle = detalle;
        this.estadoReclamo = estadoReclamo;
        this.fechaReclamo = fechaReclamo;
    }
    
    
    public Integer getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(Integer idReclamo) {
        this.idReclamo = idReclamo;
    }

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
    }

    public String getFechapedido() {
        return fechapedido;
    }

    public void setFechapedido(String fechapedido) {
        this.fechapedido = fechapedido;
    }

    public String getMotivoReclamo() {
        return motivoReclamo;
    }

    public void setMotivoReclamo(String motivoReclamo) {
        this.motivoReclamo = motivoReclamo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEstadoReclamo() {
        return estadoReclamo;
    }

    public void setEstadoReclamo(String estadoReclamo) {
        this.estadoReclamo = estadoReclamo;
    }

    public Timestamp getFechaReclamo() {
        return fechaReclamo;
    }

    public void setFechaReclamo(Timestamp fechaReclamo) {
        this.fechaReclamo = fechaReclamo;
    }
}
