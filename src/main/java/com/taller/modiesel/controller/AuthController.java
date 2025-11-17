package com.taller.modiesel.controller;

import com.taller.modiesel.model.Usuario;
import com.taller.modiesel.repository.UsuarioRepository;
import com.taller.modiesel.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody Usuario loginRequest) {
        // This will throw an exception if authentication fails
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getPassword())
        );

        // Si la autenticación fue exitosa, comprobamos si la contraseña almacenada
        // está en texto plano (o no en formato BCrypt). Si no es BCrypt, la re-hasheamos
        // con el PasswordEncoder actual y guardamos el usuario — así migramos al primer login.
        usuarioRepository.findByCorreo(loginRequest.getCorreo()).ifPresent(usuario -> {
            String stored = usuario.getPassword();
            if (stored == null || !(stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))) {
                // Re-hash the raw password and save
                usuario.setPassword(passwordEncoder.encode(loginRequest.getPassword()));
                usuarioRepository.save(usuario);
            }
        });

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getCorreo());
        return jwtTokenUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }
}
