package com.projeto.sistema.service;

import com.projeto.sistema.model.Produto;
import com.projeto.sistema.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).get();
    }

    public void salvar(Produto produto) {
        produtoRepository.saveAndFlush(produto);
    }

    public void deletar(Produto produto) {
        produtoRepository.delete(produto);
    }

    public void deletarPorId(Long id) {
        produtoRepository.deleteById(id);
    }
}
