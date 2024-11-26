package com.projeto.sistema.controller;

import com.projeto.sistema.model.Venda;
import com.projeto.sistema.model.ItemVenda;
import com.projeto.sistema.model.Produto;
import com.projeto.sistema.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class VendaController {
    @Autowired
    private VendaRepository vendaRepository;
    @Autowired
    private ItemVendaRepository itemVendaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    private List<ItemVenda> listaItemVenda = new ArrayList<ItemVenda>();

    @GetMapping("/cadastroVenda")
    public ModelAndView cadastrar(Venda venda, ItemVenda itemVenda) {
        ModelAndView mv = new ModelAndView("administrativo/vendas/cadastro");
        mv.addObject("venda", venda);
        mv.addObject("itemVenda", itemVenda);
        mv.addObject("listaItemVenda", this.listaItemVenda);
        mv.addObject("listaFuncionarios", funcionarioRepository.findAll());
        mv.addObject("listaClientes", clienteRepository.findAll());
        mv.addObject("listaProdutos", produtoRepository.findAll());
        return mv;
    }

    @GetMapping("/listarVenda")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/vendas/lista");
        mv.addObject("listaVendas", vendaRepository.findAll());
        return mv;
    }

    @GetMapping("/editarVenda/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        this.listaItemVenda = itemVendaRepository.buscarPorVenda(id);
        return cadastrar(venda.get(), new ItemVenda());
    }

//    @GetMapping("/removerVenda/{id}")
//    public ModelAndView remover(@PathVariable("id") Long id) {
//        Optional<Venda> venda = vendaRepository.findById(id);
//        vendaRepository.delete(venda.get());
//        return listar();
//    }

    @PostMapping("/salvarVenda")
    public ModelAndView salvar(String acao, Venda venda, ItemVenda itemVenda, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(venda, itemVenda);
        }

        if (acao.equals("itens")) {
            itemVenda.setValor(itemVenda.getProduto().getPrecoVenda());
            itemVenda.setSubtotal(itemVenda.getProduto().getPrecoVenda() * itemVenda.getQuantidade());
            venda.setValorTotal(venda.getValorTotal() + (itemVenda.getValor() * itemVenda.getQuantidade()));
            venda.setQuantidadeTotal(venda.getQuantidadeTotal() + itemVenda.getQuantidade());
            this.listaItemVenda.add(itemVenda);
        } else if (acao.equals("salvar")) {
            vendaRepository.saveAndFlush(venda);

            for (ItemVenda it : listaItemVenda) {
                it.setVenda(venda);
//                it.setSubtotal(it.getValor() * it.getQuantidade());

                itemVendaRepository.saveAndFlush(it);

                Optional<Produto> prod = produtoRepository.findById(it. getProduto().getId());
                Produto produto = prod.get();
                produto.setEstoque(produto.getEstoque() - it.getQuantidade());
                produto.setPrecoVenda(it.getValor());
                produtoRepository.saveAndFlush(produto);

                this.listaItemVenda = new ArrayList<>();
            }
            return cadastrar(new Venda(), new ItemVenda());
        }
        return cadastrar(venda, new ItemVenda());
    }

    public List<ItemVenda> getListaItemVenda() {
        return listaItemVenda;
    }

    public void setListaItemVenda(List<ItemVenda> listaItemVenda) {
        this.listaItemVenda = listaItemVenda;
    }
}
