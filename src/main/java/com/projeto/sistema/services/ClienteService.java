package com.projeto.sistema.services;

import com.projeto.sistema.models.Cliente;
import com.projeto.sistema.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não existe");
        }

        return clienteRepository.findById(id).get();
    }

    public void salvar(Cliente cliente) {
        clienteRepository.saveAndFlush(cliente);
    }

    public void deletar(Cliente cliente) {
        clienteRepository.delete(cliente);
    }

    public void deletarPorId(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não existe");
        }

        clienteRepository.deleteById(id);
    }
}
