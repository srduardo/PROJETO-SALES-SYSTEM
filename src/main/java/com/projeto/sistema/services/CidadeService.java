package com.projeto.sistema.services;

import com.projeto.sistema.models.Cidade;
import com.projeto.sistema.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    public Cidade buscarPorId(Long id) {
        return cidadeRepository.findById(id).get();
    }

    public void salvar(Cidade cidade) {
        cidadeRepository.saveAndFlush(cidade);
    }

    public void deletar(Cidade cidade) {
        cidadeRepository.delete(cidade);
    }

    public void deletarPorId(Long id) {
        cidadeRepository.deleteById(id);
    }
}
