package com.projeto.sistema.services;

import com.projeto.sistema.models.Fornecedor;
import com.projeto.sistema.repositories.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<Fornecedor> listar() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor buscarPorId(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new RuntimeException("Fornecedor não existe");
        }

        return fornecedorRepository.findById(id).get();
    }

    public void salvar(Fornecedor fornecedor) {
        fornecedorRepository.saveAndFlush(fornecedor);
    }

    public void deletar(Fornecedor fornecedor) {
        fornecedorRepository.delete(fornecedor);
    }

    public void deletarPorId(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new RuntimeException("Fornecedor não existe");
        }

        fornecedorRepository.deleteById(id);
    }
}
