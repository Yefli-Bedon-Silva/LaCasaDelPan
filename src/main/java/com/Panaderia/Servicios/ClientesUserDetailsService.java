package com.Panaderia.Servicios;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Repositorio.ClientesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ClientesUserDetailsService implements UserDetailsService {

    @Autowired
    private ClientesRepositorio clientesRepositorio;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Clientes cliente = clientesRepositorio.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente no encontrado"));

        return new User(
            cliente.getCorreo(),
            cliente.getContraseña(),
            cliente.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList())
        );
    }
    
}