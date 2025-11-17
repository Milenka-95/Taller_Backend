package com.taller.modiesel.service;

import com.taller.modiesel.model.Imagen;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImagenService {
    Imagen guardarImagen(Imagen imagen);
    List<Imagen> listarImagenes();
    List<Imagen> listarPorTipo(String tipo);
    Imagen obtenerPorId(Long id);
    void eliminarImagen(Long id);
}