package com.taller.modiesel.service;


import com.taller.modiesel.model.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> listarClientes();
    Cliente obtenerClientePorId(Long id);
    Cliente registrarCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
}
