package com.projeto.sistema.service;

import com.projeto.sistema.model.ItemEntrada;
import com.projeto.sistema.model.Venda;
import com.projeto.sistema.model.ItemVenda;
import com.projeto.sistema.model.Produto;
import com.projeto.sistema.repository.ItemVendaRepository;
import com.projeto.sistema.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemVendaService {

    @Autowired
    private ItemVendaRepository itemVendaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ItemVenda> listar() {
        return itemVendaRepository.findAll();
    }

    public ItemVenda buscarPorId(Long id) {
        return itemVendaRepository.findById(id).get();
    }

    public void salvar(ItemVenda itemVenda) {
        itemVendaRepository.saveAndFlush(itemVenda);
    }

    public void deletar(ItemVenda itemVenda) {
        itemVendaRepository.delete(itemVenda);
    }

    public void deletarPorId(Long id) {
        itemVendaRepository.deleteById(id);
    }

    public ItemVenda retornarAoEstoqueAoDeletarItemVenda(List<ItemVenda> listaItemVenda, Long idSequencia) {
        Venda venda = listaItemVenda.get(listaItemVenda.size() - 1).getVenda();

        for (ItemVenda it : listaItemVenda) {
            if (it.getIdSequencia().equals(idSequencia)) {
                Optional<Produto> prod = produtoRepository.findById(it.getProduto().getId());
                if (prod.isPresent()) {
                    Produto produto = prod.get();
                    produto.setEstoque(produto.getEstoque() + it.getQuantidade());
                }

                it.setVenda(venda);
                it.getVenda().setValorTotal(it.getVenda().getValorTotal() - it.getSubtotal());
                it.getVenda().setQuantidadeTotal(it.getVenda().getQuantidadeTotal() - it.getQuantidade());

                return it;
            }
        }

        return new ItemVenda();
    }
    
    public ItemVenda validarValoresDoItemVenda(ItemVenda itemVenda) {
        Double[] listaDeValores = {itemVenda.getQuantidade(), itemVenda.getValor()};
        Double[] listaDeValoresDoProduto = {1.0,  itemVenda.getProduto().getPrecoVenda()};

        for (int i = 0; i < listaDeValores.length; i++) {
            if (listaDeValores[i] == null) {
                listaDeValores[i] = listaDeValoresDoProduto[i];
            }
        }

        itemVenda.setQuantidade(listaDeValores[0]);
        itemVenda.setValor(listaDeValores[1]);
        itemVenda.setSubtotal(listaDeValores[0] * listaDeValores[1]);


        return itemVenda;
    }

    public void salvarListaItemVenda(List<ItemVenda> listaItemVenda) {
        itemVendaRepository.saveAll(listaItemVenda);
    }

    public List<ItemVenda> reajustarIdSequencia(List<ItemVenda> listaItemVenda) {

        for (int i = 0; i < listaItemVenda.size(); i++) {
            listaItemVenda.get(i).setIdSequencia(i + 1L);
        }

        return  listaItemVenda;
    }

    public List<ItemVenda> adicionarItemVenda(List<ItemVenda> listaItemVenda, Venda venda, ItemVenda itemVenda) {
        itemVenda = validarValoresDoItemVenda(itemVenda);
        
        venda.setValorTotal(venda.getValorTotal() + itemVenda.getSubtotal());
        venda.setQuantidadeTotal(venda.getQuantidadeTotal() + itemVenda.getQuantidade());

        if (listaItemVenda.isEmpty()) {
            itemVenda.setIdSequencia(1L);
        } else {
            itemVenda.setIdSequencia(listaItemVenda.get(listaItemVenda.size() - 1).getIdSequencia() + 1L);
        }

        listaItemVenda.add(itemVenda);

        for (ItemVenda it : listaItemVenda) {
            it.setVenda(venda);
        }

        return listaItemVenda;
    }
}
