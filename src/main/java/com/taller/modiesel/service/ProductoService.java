package com.taller.modiesel.service;

import com.taller.modiesel.model.Producto;

import java.util.List;

public interface ProductoService {
    List<Producto> listarProductos();
    Producto obtenerProductoPorId(Long id);
    Producto registrarProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
}
