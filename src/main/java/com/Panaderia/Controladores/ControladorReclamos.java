package com.Panaderia.Controladores;

import com.Panaderia.Modelo.Clientes;
import com.Panaderia.Modelo.Reclamo;
import com.Panaderia.Repositorio.ClientesRepositorio;
import com.Panaderia.Repositorio.ReclamoRepositorio;
import com.Panaderia.Servicios.ClientesServicio;
import com.Panaderia.dto.ReclamoDTO;
import jakarta.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private ClientesServicio clientesServicio;

    @GetMapping("/reclamos")
    public String mostrarFormularioCompra(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            String correo = authentication.getName();
            Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);

            if (clienteOpt.isPresent()) {
                Clientes cliente = clienteOpt.get();

                // Crear ReclamoDTO y llenarlo con los datos del cliente
                ReclamoDTO reclamoDTO = new ReclamoDTO();
                reclamoDTO.setCorreo(cliente.getCorreo());
                reclamoDTO.setDni(cliente.getDni());
                reclamoDTO.setNombre(cliente.getNombreCli());
                reclamoDTO.setApellidos(cliente.getApellidosCli());
                reclamoDTO.setDireccion(cliente.getDireccion());
                reclamoDTO.setTelefono(cliente.getTelefono());

                model.addAttribute("reclamoDTO", reclamoDTO); // se puede usar en Thymeleaf
                model.addAttribute("nombreCliente", cliente.getNombreCli());

                return "FrmReclamos";
            }
        }

        // Si no está autenticado
        model.addAttribute("nombreCliente", "Invitado");
        return "redirect:/login"; // o redirige a login si deseas
    }

    @PostMapping("/Nuevo")
    public ResponseEntity<?> crearReclamo(@RequestBody ReclamoDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.badRequest().body("Usuario no autenticado");
        }

        String correo = authentication.getName();
        Optional<Clientes> clienteOpt = clientesServicio.findClienteByCorreo(correo);

        if (clienteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }

        Clientes cliente = clienteOpt.get();

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
