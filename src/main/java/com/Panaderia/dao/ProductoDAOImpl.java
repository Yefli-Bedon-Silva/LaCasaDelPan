package com.Panaderia.dao;

import com.Panaderia.Modelo.Producto;
import com.Panaderia.dao.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductoDAOImpl implements ProductoDAO {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Override
    public List<Producto> get() {
        return productoRepositorio.findAll();
    }

    @Override
    public Producto get(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    @Override
    public void save(Producto producto) {
        productoRepositorio.save(producto);
    }

    @Override
    public void update(Producto producto) {
        productoRepositorio.save(producto);
    }

    @Override
    public void delete(Long id) {
        productoRepositorio.deleteById(id);
    }

    @Override
    public List<Producto> getByCategoria(String categoria) {
        return productoRepositorio.findByCategoria(categoria);
    }
}