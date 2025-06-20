package com.Panaderia.Servicios;

import com.Panaderia.Modelo.Reclamo;
import java.util.List;

public interface ReclamoServicio {
    List<Reclamo> get();
    Reclamo get(Integer id);
    void save(Reclamo producto);
    void update(Reclamo producto);
    void delete(Integer id);
    List<Reclamo> getByEstado(String estado);
    /*List<Reclamo> buscar(String texto);*/
   /* List<Reclamo> buscarPorTextoYCategoria(String texto, String categoria);*/
  
}
