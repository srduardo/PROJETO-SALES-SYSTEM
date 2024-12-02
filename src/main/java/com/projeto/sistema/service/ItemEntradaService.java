package com.projeto.sistema.service;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.model.ItemEntrada;
import com.projeto.sistema.model.ItemVenda;
import com.projeto.sistema.model.Produto;
import com.projeto.sistema.repository.ItemEntradaRepository;
import com.projeto.sistema.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemEntradaService {

    @Autowired
    private ItemEntradaRepository itemEntradaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ItemEntrada> listar() {
        return itemEntradaRepository.findAll();
    }

    public ItemEntrada buscarPorId(Long id) {
        return itemEntradaRepository.findById(id).get();
    }

    public void salvar(ItemEntrada itemEntrada) {
        itemEntradaRepository.saveAndFlush(itemEntrada);
    }

    public void deletar(ItemEntrada itemEntrada) {
        itemEntradaRepository.delete(itemEntrada);
    }

    public void deletarPorId(Long id) {
        itemEntradaRepository.deleteById(id);
    }

    public ItemEntrada darBaixaNoEstoqueAoDeletarItemEntrada(List<ItemEntrada> listaItemEntrada, Long idSequencia) {

        for (ItemEntrada it : listaItemEntrada) {
            if (it.getIdSequencia().equals(idSequencia)) {
                Optional<Produto> prod = produtoRepository.findById(it.getProduto().getId());
                if (prod.isPresent()) {
                    Produto produto = prod.get();
                    produto.setEstoque(produto.getEstoque() - it.getQuantidade());
                }

                it.getEntrada().setValorTotal(it.getEntrada().getValorTotal() - (it.getValor() * it.getQuantidade()));
                it.getEntrada().setQuantidadeTotal(it.getEntrada().getQuantidadeTotal() - it.getQuantidade());

                return it;
            }
        }

        return new ItemEntrada();
    }

    public ItemEntrada validarValoresItemEntrada(ItemEntrada itemEntrada) {
        Double[] listaDeValores = {itemEntrada.getQuantidade(), itemEntrada.getValorCusto(), itemEntrada.getValor()};
        Double[] listaDeValoresDoProduto = {1.0, itemEntrada.getProduto().getPrecoCusto(), itemEntrada.getProduto().getPrecoVenda()};

        for (int i = 0; i < listaDeValores.length; i++) {
            if (listaDeValores[i] == null) {
                listaDeValores[i] = listaDeValoresDoProduto[i];
            }
        }

        itemEntrada.setQuantidade(listaDeValores[0]);
        itemEntrada.setValorCusto(listaDeValores[1]);
        itemEntrada.setValor(listaDeValores[2]);

        return itemEntrada;
    }

    public List<ItemEntrada> reajustarIdSequencia(List<ItemEntrada> listaItemEntrada) {

        for (int i = 0; i < listaItemEntrada.size(); i++) {
            listaItemEntrada.get(i).setIdSequencia(i + 1L);
        }

        return listaItemEntrada;
    }

    public void adicionarItemEntrada(List<ItemEntrada> listaItemEntrada, Entrada entrada, ItemEntrada itemEntrada) {
        itemEntrada = validarValoresItemEntrada(itemEntrada);
        entrada.setValorTotal(entrada.getValorTotal() + (itemEntrada.getValor() * itemEntrada.getQuantidade()));
        entrada.setQuantidadeTotal(entrada.getQuantidadeTotal() + itemEntrada.getQuantidade());

        if (listaItemEntrada.isEmpty()) {
            itemEntrada.setIdSequencia(1L);
        } else {
            itemEntrada.setIdSequencia(listaItemEntrada.get(listaItemEntrada.size() - 1).getIdSequencia() + 1L);
        }

        itemEntrada.setEntrada(entrada);
        listaItemEntrada.add(itemEntrada);
    }
}
