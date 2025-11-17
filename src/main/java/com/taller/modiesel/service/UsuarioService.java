package com.taller.modiesel.service;
import com.taller.modiesel.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UsuarioService {
    List<Usuario> listarUsuarios();
    Usuario obtenerUsuarioPorId(Long id);
    Usuario registrarUsuario(Usuario usuario);
    Usuario actualizarUsuario(Long id, Usuario usuario);
    void eliminarUsuario(Long id);
    Usuario buscarPorCorreo(String correo);
}
