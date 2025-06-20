package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Reclamo;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.Repositorio.ReclamoRepositorio;
import com.Panaderia.dto.ReclamoDTO;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.core.annotation.AuthenticationPrincipal;
*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ControladorReclamos {
    
    @Autowired
    private ReclamoRepositorio reclamoRepositorio;
    
    @Autowired
    private ClientesRepositorio clienterepositorio;
    
    @GetMapping("/reclamos")
    public String mostrarFormularioCompra(Model model, HttpSession session) {
        Clientes cliente = (Clientes) session.getAttribute("cliente");
        if (cliente != null) {
            model.addAttribute("nombreCli", cliente.getNombreCli());
            model.addAttribute("apellidosCli", cliente.getApellidosCli());
            model.addAttribute("dni", cliente.getDni());
            model.addAttribute("direccion", cliente.getDireccion());
            model.addAttribute("telefono", cliente.getTelefono());
            model.addAttribute("correo", cliente.getCorreo());
        }
        return "FrmReclamos";
    }

    @PostMapping("/Nuevo")
    public ResponseEntity<?> crearReclamo(@RequestBody ReclamoDTO dto,
            /*@AuthenticationPrincipal*/ HttpSession session) {

        Clientes cliente = (Clientes) session.getAttribute("cliente");
 
    if (cliente == null) {
        return ResponseEntity.badRequest().body("Usuario no autenticado");
    }
        Reclamo reclamo = new Reclamo();
        reclamo.setFechapedido(dto.getFechapedido());
        reclamo.setMotivoReclamo(dto.getMotivoReclamo());
        reclamo.setDetalle(dto.getDetalle());
        reclamo.setEstadoReclamo("pendiente");
        reclamo.setIdCliente(cliente);
        reclamo.setFechaReclamo(new Timestamp(System.currentTimeMillis()));
        reclamoRepositorio.save(reclamo);
        return ResponseEntity.ok("Reclamo registrado con éxito");
    }
}
