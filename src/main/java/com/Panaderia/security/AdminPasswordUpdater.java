/*package com.Panaderia.security;
import com.Panaderia.Repositorio.ClientesRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminPasswordUpdater implements CommandLineRunner {

    @Autowired
    private ClientesRepositorio clientesRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminCorreo = "admin@admin.com";
        String rawPassword = "admin123";

        var adminOpt = clientesRepo.findByCorreo(adminCorreo);
        if (adminOpt.isPresent()) {
            var admin = adminOpt.get();
            admin.setContraseña(passwordEncoder.encode(rawPassword));
            clientesRepo.save(admin);
            System.out.println("Contraseña del admin actualizada correctamente.");
        } else {
            System.out.println("Admin no encontrado para actualizar contraseña.");
        }
    }
}*/