package com.projeto.sistema.controller;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.model.ItemEntrada;
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

    @GetMapping("/editarItemEntrada/{idSequencia}")
    public ModelAndView editarItemEntrada(@PathVariable("idSequencia") Long idSequencia) {
        ItemEntrada itemEntrada = null;

        for (ItemEntrada it : this.listaItemEntrada) {
            if (it.getIdSequencia().equals(idSequencia)) {
                ModelAndView mv = new ModelAndView("/administrativo/produtos/cadastro");
                mv.addObject("produto", it.getProduto());
                return mv;
            }

            itemEntrada = it;
        }

        return cadastrar(itemEntrada.getEntrada(), new ItemEntrada());
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
            itemEntradaService.adicionarItemEntrada(listaItemEntrada, entrada, itemEntrada);
        } else if (acao.equals("salvar")) {
            entradaService.salvar(entrada);
            entradaService.adicionarItensAoEstoqueAoSalvarEntrada(entrada, this.listaItemEntrada);
            this.listaItemEntrada = new ArrayList<>();
            return cadastrar(new Entrada(), new ItemEntrada());
        }

        return cadastrar(entrada, new ItemEntrada());
    }
}
