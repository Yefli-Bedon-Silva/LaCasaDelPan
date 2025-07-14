package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Reclamo;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.Servicios.ReclamoServicio;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorAdminReclamos {

    private final ReclamoServicio reclamoServicio;
    private final ClientesServicio clientesServicio;

    public ControladorAdminReclamos(ReclamoServicio reclamoServicio, ClientesServicio clientesServicio) {
        this.reclamoServicio = reclamoServicio;
        this.clientesServicio = clientesServicio;
    }

    @GetMapping("/adminreclamos")
    public String listarReclamos(Model model) {
        agregarNombreUsuarioAlModelo(model);
        List<Reclamo> listarReclamo = reclamoServicio.get();
        model.addAttribute("reclamos", listarReclamo);
        return "Adminreclamos";
    }

    private void agregarNombreUsuarioAlModelo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String correo = auth.getName();
            clientesServicio.findClienteByCorreo(correo).ifPresentOrElse(
                    cliente -> model.addAttribute("nombreUsuario", cliente.getNombreCli()),
                    () -> model.addAttribute("nombreUsuario", correo)
            );
        } else {
            model.addAttribute("nombreUsuario", "Invitado");
        }
    }

    @PostMapping("/adminreclamos/guardar")
    public String guardarReclamo(@RequestParam("idReclamo") Integer idReclamo,
            @RequestParam("estadoReclamo") String estadoReclamo) {

        Reclamo reclamoExistente = reclamoServicio.get(idReclamo);

        if (reclamoExistente != null) {
            reclamoExistente.setEstadoReclamo(estadoReclamo);
            reclamoServicio.save(reclamoExistente);
        }

        return "redirect:/adminreclamos";
    }

    /*@GetMapping("/adminreclamos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model) {
        model.addAttribute("reclamo", reclamoServicio.get(id));
        return "adminreclamos_form";
    }*/
 /*
    @GetMapping("/adminreclamos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        reclamoServicio.delete(id);
        return "redirect:/adminreclamos";
    }*/
}
