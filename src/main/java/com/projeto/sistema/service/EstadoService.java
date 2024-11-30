package com.projeto.sistema.service;

import com.projeto.sistema.model.Estado;
import com.projeto.sistema.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    public Estado buscarPorId(Long id) {
        return estadoRepository.findById(id).get();
    }

    public void salvar(Estado estado) {
        estadoRepository.saveAndFlush(estado);
    }

    public void deletar(Estado estado) {
        estadoRepository.delete(estado);
    }

    public void deletarPorId(Long id) {
        estadoRepository.deleteById(id);
    }
}
