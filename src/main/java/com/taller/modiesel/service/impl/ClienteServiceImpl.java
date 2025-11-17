package com.taller.modiesel.service.impl;

import com.taller.modiesel.external.ReniecApiService;
import com.taller.modiesel.model.Cliente;
import com.taller.modiesel.repository.ClienteRepository;
import com.taller.modiesel.service.ClienteService;
import com.taller.modiesel.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
    }

    @Autowired
    private ReniecApiService reniecApiService;

    public Cliente registrarCliente(Cliente cliente) {
        try {
            Map<String, Object> respuesta = reniecApiService.consultarRuc(cliente.getRuc());
            if (respuesta != null && Boolean.TRUE.equals(respuesta.get("success"))) {
                Map<String, Object> datos = (Map<String, Object>) respuesta.get("data");
                if (datos != null) {
                    String razonSocial = (String) datos.get("nombre_o_razon_social");
                    if (razonSocial == null) {
                        razonSocial = (String) datos.get("razonSocial");
                    }
                    if (razonSocial == null) {
                        razonSocial = (String) datos.get("RazonSocial");
                    }
                    if (razonSocial != null) {
                        cliente.setRazonSocial(razonSocial);
                    }
                }
            }
        } catch (Exception e) {
            // Log error but continue with registration
            System.err.println("Error consultando RUC: " + e.getMessage());
        }
        return clienteRepository.save(cliente);
    }
    @Override
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente existente = obtenerClientePorId(id);
        existente.setRazonSocial(cliente.getRazonSocial());
        existente.setEstado(cliente.getEstado());
        existente.setTelefono(cliente.getTelefono());
        existente.setCorreo(cliente.getCorreo());
        return clienteRepository.save(existente);
    }

    @Override
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
