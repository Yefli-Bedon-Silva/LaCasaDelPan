package com.Panaderia.dao;

import com.Panaderia.Modelo.Reclamo;
import java.util.List;

public interface ReclamoDAO {

    List<Reclamo> get();

    Reclamo get(Integer id);

    void save(Reclamo producto);

    void update(Reclamo producto);

    void delete(Integer id);

    List<Reclamo> getByEstado(String Estado);
}
