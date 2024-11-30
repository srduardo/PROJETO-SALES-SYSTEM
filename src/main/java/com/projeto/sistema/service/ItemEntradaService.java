package com.projeto.sistema.service;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.repository.EntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;

    public List<Entrada> listar() {
        return entradaRepository.findAll();
    }

    public Entrada buscarPorId(Long id) {
        return entradaRepository.findById(id).get();
    }

    public void salvar(Entrada entrada) {
        entradaRepository.saveAndFlush(entrada);
    }

    public void deletar(Entrada entrada) {
        entradaRepository.delete(entrada);
    }

    public void deletarPorId(Long id) {
        entradaRepository.deleteById(id);
    }
}
