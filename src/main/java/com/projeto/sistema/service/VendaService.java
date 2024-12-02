package com.projeto.sistema.service;

import com.projeto.sistema.model.Venda;
import com.projeto.sistema.model.ItemVenda;
import com.projeto.sistema.model.Produto;
import com.projeto.sistema.repository.VendaRepository;
import com.projeto.sistema.repository.ItemVendaRepository;
import com.projeto.sistema.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository VendaRepository;
    @Autowired
    private ItemVendaRepository itemVendaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Venda> listar() {
        return VendaRepository.findAll();
    }

    public Venda buscarPorId(Long id) {
        if (VendaRepository.findById(id).isPresent()) {
            return VendaRepository.findById(id).get();
        }

        return null;
    }

    public void salvar(Venda Venda) {
        VendaRepository.saveAndFlush(Venda);
    }

    public void deletar(Venda Venda) {
        VendaRepository.delete(Venda);
    }

    public void deletarPorId(Long id) {
        VendaRepository.deleteById(id);
    }


    public List<ItemVenda> adicionarAoEstoqueAoDeletarVenda(Venda Venda) {
        List<ItemVenda> listaItemVenda = itemVendaRepository.findAll();

        for (ItemVenda it : listaItemVenda) {
            if (it.getVenda().getId().equals(Venda.getId())) {
                it.getProduto().setEstoque(it.getProduto().getEstoque() + it.getQuantidade());
                itemVendaRepository.deleteById(it.getId());
            }
        }

        listaItemVenda.clear();
        return listaItemVenda;
    }

    public void removerItensDoEstoqueAoRealizarVenda(Venda Venda, List<ItemVenda> listaItemVenda) {
        for (ItemVenda it : listaItemVenda) {
            it.setVenda(Venda);
            itemVendaRepository.saveAndFlush(it);

            Optional<Produto> prod = produtoRepository.findById(it.getProduto().getId());
            if (prod.isPresent()) {
                Produto produto = prod.get();
                produto.setEstoque(produto.getEstoque() - it.getQuantidade());
                produtoRepository.saveAndFlush(produto);
            }

        }
    }
}
