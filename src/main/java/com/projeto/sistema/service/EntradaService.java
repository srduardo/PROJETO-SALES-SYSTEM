package com.projeto.sistema.service;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.model.ItemEntrada;
import com.projeto.sistema.model.Produto;
import com.projeto.sistema.repository.EntradaRepository;
import com.projeto.sistema.repository.ItemEntradaRepository;
import com.projeto.sistema.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;
    @Autowired
    private ItemEntradaRepository itemEntradaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Entrada> listar() {
        return entradaRepository.findAll();
    }

    public Entrada buscarPorId(Long id) {
        if (entradaRepository.findById(id).isPresent()) {
            return entradaRepository.findById(id).get();
        }

        return null;
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


    public List<ItemEntrada> darBaixaNoEstoqueAoDeletarEntrada(Entrada entrada) {
        List<ItemEntrada> listaItemEntrada = itemEntradaRepository.findAll();

        for (ItemEntrada it : listaItemEntrada) {
            if (it.getEntrada().getId().equals(entrada.getId())) {
                it.getProduto().setEstoque(it.getProduto().getEstoque() - it.getQuantidade());
                itemEntradaRepository.deleteById(it.getId());
            }
        }

        listaItemEntrada.clear();
        return listaItemEntrada;
    }

    public void adicionarItensAoEstoqueAoSalvarEntrada(Entrada entrada, List<ItemEntrada> listaItemEntrada) {
        for (ItemEntrada it : listaItemEntrada) {
            it.setEntrada(entrada);
            itemEntradaRepository.saveAndFlush(it);

            Optional<Produto> prod = produtoRepository.findById(it.getProduto().getId());

            if (prod.isPresent()) {
                Produto produto = prod.get();
                produto.setEstoque(produto.getEstoque() + it.getQuantidade());
                produto.setPrecoVenda(it.getValor());
                produto.setPrecoCusto(it.getValorCusto());
                produtoRepository.saveAndFlush(produto);
            }
        }
    }
}
