package com.projeto.sistema.controllers;

import com.projeto.sistema.models.Venda;
import com.projeto.sistema.models.ItemVenda;
import com.projeto.sistema.repositories.VendaRepository;
import com.projeto.sistema.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class VendaController {
    @Autowired
    private VendaService vendaService;
    @Autowired
    private ItemVendaService itemVendaService;
    @Autowired
    private ProdutoService produtoService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private ClienteService clienteService;

    private List<ItemVenda> listaItemVenda = new ArrayList<ItemVenda>();
    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping("/cadastroVenda")
    public ModelAndView cadastrar(Venda venda, ItemVenda itemVenda) {
        ModelAndView mv = new ModelAndView("administrativo/vendas/cadastro");
        if (this.listaItemVenda.isEmpty()) {
            mv.addObject("venda", venda);
        } else {
            mv.addObject("venda", this.listaItemVenda.get(this.listaItemVenda.size() - 1).getVenda());
        }
        mv.addObject("itemVenda", itemVenda);
        mv.addObject("listaItemVenda", this.listaItemVenda);
        mv.addObject("listaFuncionarios", funcionarioService.listar());
        mv.addObject("listaClientes", clienteService.listar());
        mv.addObject("listaProdutos", produtoService.listar());
        return mv;
    }

    @GetMapping("/listarVenda")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/vendas/lista");
        mv.addObject("listaVendas", vendaService.listar());
        return mv;
    }

    @GetMapping("/editarVenda/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Venda venda = vendaService.buscarPorId(id);
        this.listaItemVenda = itemVendaService.listar();
        return cadastrar(venda, new ItemVenda());
    }

    @GetMapping("/editarItemVenda/{idSequencia}")
    public ModelAndView editarItemVenda(@PathVariable("idSequencia") Long idSequencia) {
        ItemVenda itemVenda = null;

        for (ItemVenda it : this.listaItemVenda) {
            if (it.getIdSequencia().equals(idSequencia)) {
                ModelAndView mv = new ModelAndView("administrativo/produtos/cadastro");
                mv.addObject("produto", it.getProduto());
                return mv;
            }

            itemVenda = it;
        }

        return cadastrar(itemVenda.getVenda(), new ItemVenda());
    }

    @GetMapping("/removerVenda/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Venda venda = vendaService.buscarPorId(id);
        this.listaItemVenda = vendaService.adicionarAoEstoqueAoDeletarVenda(venda);
        vendaService.deletar(venda);

        return listar();
    }

    @GetMapping("/removerItemVenda/{idSequencia}")
    public ModelAndView removerItemVenda(@PathVariable("idSequencia") Long idSequencia) {
        ItemVenda itemVenda = itemVendaService.retornarAoEstoqueAoDeletarItemVenda(this.listaItemVenda, idSequencia);
        Venda venda = itemVenda.getVenda();
        listaItemVenda.remove(itemVenda);
        listaItemVenda = itemVendaService.reajustarIdSequencia(this.listaItemVenda);

        if (venda.getId() != null && vendaService.buscarPorId(venda.getId()) == null) {
            return cadastrar(new Venda(), new ItemVenda());
        }

        return cadastrar(venda, new ItemVenda());
    }

    @PostMapping("/salvarVenda")
    public ModelAndView salvar(String acao, Venda venda, ItemVenda itemVenda, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(venda, itemVenda);
        }

        if (acao.equals("itens")) {
            this.listaItemVenda = itemVendaService.adicionarItemVenda(this.listaItemVenda, venda, itemVenda);
        } else if (acao.equals("salvar")) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
            venda.setDataVenda(df.format(new Date()));
            vendaService.salvar(venda);
            vendaService.removerItensDoEstoqueAoRealizarVenda(venda, this.listaItemVenda);
            this.listaItemVenda = new ArrayList<>();
            return cadastrar(new Venda(), new ItemVenda());
        }

        return cadastrar(venda, new ItemVenda());
    }
}
