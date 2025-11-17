package com.taller.modiesel.controller;

import com.taller.modiesel.model.Inventario;
import com.taller.modiesel.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:3000")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<Inventario> listar() {
        return inventarioService.listarInventario();
    }

    @PostMapping
    public Inventario registrar(@RequestBody Inventario inventario) {
        return inventarioService.registrarProducto(inventario);
    }

    @PutMapping("/{id}")
    public Inventario actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        inventario.setId(id);
        return inventarioService.actualizarInventario(id, inventario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
    }
}
