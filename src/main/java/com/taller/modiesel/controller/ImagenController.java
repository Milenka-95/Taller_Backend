package com.taller.modiesel.controller;

import com.taller.modiesel.model.Imagen;
import com.taller.modiesel.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/imagenes", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:3000")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Imagen> subirImagen(@RequestBody Imagen imagen) {
        Imagen nueva = imagenService.guardarImagen(imagen);
        return ResponseEntity.ok(nueva);
    }

    @GetMapping
    public ResponseEntity<List<Imagen>> listarTodas() {
        return ResponseEntity.ok(imagenService.listarImagenes());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Imagen>> listarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(imagenService.listarPorTipo(tipo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagen> obtenerPorId(@PathVariable Long id) {
        Imagen imagen = imagenService.obtenerPorId(id);
        return imagen != null ? ResponseEntity.ok(imagen) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        imagenService.eliminarImagen(id);
        return ResponseEntity.noContent().build();
    }
}
