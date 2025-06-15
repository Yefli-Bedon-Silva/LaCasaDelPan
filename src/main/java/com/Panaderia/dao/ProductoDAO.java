package com.Panaderia.dao;

import com.Panaderia.Modelo.Producto;

import java.util.List;

public interface ProductoDAO {
   List<Producto> get();
    Producto get(Long id);
    void save(Producto producto);
    void update(Producto producto);
    void delete(Long id);
    List<Producto> getByCategoria(String categoria);
}