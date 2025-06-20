package com.Panaderia.Servicios;

import com.Panaderia.dao.ReclamoDAO;
import com.Panaderia.Modelo.Reclamo;
/*import com.Panaderia.Repositorio.ReclamoRepositorio;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ReclamoServicioImpl implements ReclamoServicio {

    @Autowired
    private ReclamoDAO reclamoDAO;
    /*@Autowired
    private ReclamoRepositorio reclamoRepositorio;*/

    @Transactional
    @Override
    public List<Reclamo> get() {
        return reclamoDAO.get();
    }

    @Transactional
    @Override
    public Reclamo get(Integer id) {
        return reclamoDAO.get(id);
    }

    @Transactional
    @Override
    public void save(Reclamo producto) {
        reclamoDAO.save(producto);
    }

    @Transactional
    @Override
    public void update(Reclamo producto) {
        reclamoDAO.update(producto);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        reclamoDAO.delete(id);
    }

    @Transactional
    @Override
    public List<Reclamo> getByEstado(String estado) {
        return reclamoDAO.getByEstado(estado);
    }

}
