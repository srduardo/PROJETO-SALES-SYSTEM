package com.projeto.sistema.controller;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.model.ItemEntrada;
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
public class EntradaController {
    @Autowired
    private EntradaRepository entradaRepository;
    @Autowired
    private ItemEntradaRepository itemEntradaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;

    private List<ItemEntrada> listaItemEntrada = new ArrayList<ItemEntrada>();

    @GetMapping("/cadastroEntrada")
    public ModelAndView cadastrar(Entrada entrada, ItemEntrada itemEntrada) {
        ModelAndView mv = new ModelAndView("administrativo/entradas/cadastro");
        mv.addObject("entrada", entrada);
        mv.addObject("itemEntrada", itemEntrada);
        mv.addObject("listaItemEntrada", this.listaItemEntrada);
        mv.addObject("listaFuncionarios", funcionarioRepository.findAll());
        mv.addObject("listaFornecedores", fornecedorRepository.findAll());
        mv.addObject("listaProdutos", produtoRepository.findAll());
        return mv;
    }

    @GetMapping("/listarEntrada")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/entradas/lista");
        mv.addObject("listaEntradas", entradaRepository.findAll());
        return mv;
    }

//    @GetMapping("/editarEntrada/{id}")
//    public ModelAndView editar(@PathVariable("id") Long id) {
//        Optional<Entrada> entrada = entradaRepository.findById(id);
//        return cadastrar(entrada.get());
//    }

//    @GetMapping("/removerEntrada/{id}")
//    public ModelAndView remover(@PathVariable("id") Long id) {
//        Optional<Entrada> entrada = entradaRepository.findById(id);
//        entradaRepository.delete(entrada.get());
//        return listar();
//    }

    @PostMapping("/salvarEntrada")
    public ModelAndView salvar(String acao, Entrada entrada, ItemEntrada itemEntrada, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(entrada, itemEntrada);
        }

        if (acao.equals("itens")) {
            this.listaItemEntrada.add(itemEntrada);
            entrada.setValorTotal(entrada.getValorTotal() + (itemEntrada.getValor() * itemEntrada.getQuantidade()));
            entrada.setQuantidadeTotal(entrada.getQuantidadeTotal() + itemEntrada.getQuantidade());
        } else if (acao.equals("salvar")) {
            entradaRepository.saveAndFlush(entrada);

            for (ItemEntrada it : listaItemEntrada) {
                it.setEntrada(entrada);
                itemEntradaRepository.saveAndFlush(it);

                Optional<Produto> prod = produtoRepository.findById(it. getProduto().getId());
                Produto produto = prod.get();
                produto.setEstoque(produto.getEstoque() + it.getQuantidade());
                produto.setPrecoVenda(it.getValor());
                produto.setPrecoCusto(it.getValorCusto());
                produtoRepository.saveAndFlush(produto);

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
