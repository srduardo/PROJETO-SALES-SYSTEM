package com.projeto.sistema.controller;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.model.ItemEntrada;
import com.projeto.sistema.model.Produto;
import com.projeto.sistema.service.*;
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
public class EntradaController {
    @Autowired
    private EntradaService entradaService;
    @Autowired
    private ItemEntradaService itemEntradaService;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private FornecedorService fornecedorService;

    private List<ItemEntrada> listaItemEntrada = new ArrayList<ItemEntrada>();

    @GetMapping("/cadastroEntrada")
    public ModelAndView cadastrar(Entrada entrada, ItemEntrada itemEntrada) {
        ModelAndView mv = new ModelAndView("administrativo/entradas/cadastro");
        mv.addObject("entrada", entrada);
        mv.addObject("itemEntrada", itemEntrada);
        mv.addObject("listaItemEntrada", this.listaItemEntrada);
        mv.addObject("listaFuncionarios", funcionarioService.listar());
        mv.addObject("listaFornecedores", fornecedorService.listar());
        mv.addObject("listaProdutos", produtoService.listar());
        return mv;
    }

    @GetMapping("/listarEntrada")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/entradas/lista");
        mv.addObject("listaEntradas", entradaService.listar());
        return mv;
    }

    @GetMapping("/editarEntrada/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Entrada entrada = entradaService.buscarPorId(id);
        this.listaItemEntrada = itemEntradaService.listar();
        return cadastrar(entrada, new ItemEntrada());
    }

    @GetMapping("/removerEntrada/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Entrada entrada = entradaService.buscarPorId(id);
        this.listaItemEntrada = entradaService.darBaixaNoEstoqueAoDeletarEntrada(entrada);
        entradaService.deletar(entrada);

        return listar();
    }

    @GetMapping("/removerItemEntrada/{idSequencia}")
    public ModelAndView removerItemEntrada(@PathVariable("idSequencia") Long idSequencia) {
        ItemEntrada itemEntrada =  itemEntradaService.darBaixaNoEstoqueAoDeletarItemEntrada(this.listaItemEntrada, idSequencia);
        Entrada entrada = itemEntrada.getEntrada();
        listaItemEntrada.remove(itemEntrada);

        if (entrada.getId() != null && entradaService.buscarPorId(entrada.getId()) == null) {
            return cadastrar(new Entrada(), new ItemEntrada());
        }

        return cadastrar(entrada, new ItemEntrada());
    }

    @PostMapping("/salvarEntrada")
    public ModelAndView salvar(String acao, Entrada entrada, ItemEntrada itemEntrada, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(entrada, itemEntrada);
        }

        if (acao.equals("itens")) {
            entrada.setValorTotal(entrada.getValorTotal() + (itemEntrada.getValor() * itemEntrada.getQuantidade()));
            entrada.setQuantidadeTotal(entrada.getQuantidadeTotal() + itemEntrada.getQuantidade());
            if (this.listaItemEntrada.isEmpty()) {
                itemEntrada.setIdSequencia(1L);
            } else {
                itemEntrada.setIdSequencia(this.listaItemEntrada.get(this.listaItemEntrada.size() - 1).getIdSequencia() + 1L);
            }
            itemEntrada.setEntrada(entrada);
            this.listaItemEntrada.add(itemEntrada);
        } else if (acao.equals("salvar")) {
            entradaService.salvar(entrada);

            for (ItemEntrada it : listaItemEntrada) {
                it.setEntrada(entrada);
                itemEntradaService.salvar(it);

                Produto prod = produtoService.buscarPorId(it.getProduto().getId());
                Produto produto = prod;
                produto.setEstoque(produto.getEstoque() + it.getQuantidade());
                produto.setPrecoVenda(it.getValor());
                produto.setPrecoCusto(it.getValorCusto());
                produtoService.salvar(produto);

                this.listaItemEntrada = new ArrayList<>();
            }
            return cadastrar(new Entrada(), new ItemEntrada());
        }
        return cadastrar(entrada, new ItemEntrada());
    }

    public List<ItemEntrada> getListaItemEntrada() {
        return listaItemEntrada;
    }

    public void setListaItemEntrada(List<ItemEntrada> listaItemEntrada) {
        this.listaItemEntrada = listaItemEntrada;
    }
}
