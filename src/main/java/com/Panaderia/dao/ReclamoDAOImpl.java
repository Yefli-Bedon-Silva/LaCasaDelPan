package com.Panaderia.dao;

import com.Panaderia.Modelo.Reclamo;
import com.Panaderia.Repositorio.ReclamoRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReclamoDAOImpl implements ReclamoDAO {

    @Autowired
    private ReclamoRepositorio reclamoRepositorio;

    @Override
    public List<Reclamo> get() {
        return reclamoRepositorio.findAll();
    }

    @Override
    public Reclamo get(Integer id) {
        return reclamoRepositorio.findById(id).orElse(null);
    }

    @Override
    public void save(Reclamo producto) {
        reclamoRepositorio.save(producto);
    }

    @Override
    public void update(Reclamo producto) {
        reclamoRepositorio.save(producto);
    }

    @Override
    public void delete(Integer id) {
        reclamoRepositorio.deleteById(id);
    }

    @Override
    public List<Reclamo> getByEstado(String Estado) {
        return reclamoRepositorio.findByEstadoReclamo(Estado);
    }
}
