package com.taller.modiesel.config;

import com.taller.modiesel.model.Usuario;
import com.taller.modiesel.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Runner opcional que migra todas las contraseñas que no estén en formato BCrypt.
 * Activar con la propiedad: -Dapp.migrate-passwords=true o en application.properties
 * app.migrate-passwords=true
 */
@Component
@ConditionalOnProperty(prefix = "app", name = "migrate-passwords", havingValue = "true")
public class PasswordMigrationRunner implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<Usuario> usuarios = usuarioRepository.findAll();
        int migrated = 0;
        for (Usuario u : usuarios) {
            String stored = u.getPassword();
            if (stored == null) continue;
            // Heurística: BCrypt hashes empiezan con $2a$, $2b$ o $2y$
            if (!(stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))) {
                // Re-hash y guardar
                u.setPassword(passwordEncoder.encode(stored));
                usuarioRepository.save(u);
                migrated++;
            }
        }
        System.out.println("PasswordMigrationRunner: migrated passwords = " + migrated);
    }
}

