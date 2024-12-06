package com.projeto.sistema.controllers;

import com.projeto.sistema.models.Fornecedor;
import com.projeto.sistema.services.CidadeService;
import com.projeto.sistema.services.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FornecedorController {
    @Autowired
    private FornecedorService fornecedorService;
    @Autowired
    private CidadeService cidadeService;

    @GetMapping("/cadastroFornecedor")
    public ModelAndView cadastrar(Fornecedor fornecedor) {
        ModelAndView mv = new ModelAndView("administrativo/fornecedores/cadastro");
        mv.addObject("fornecedor", fornecedor);
        mv.addObject("listaCidades", cidadeService.listar());
        return mv;
    }

    @GetMapping("/listarFornecedor")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/fornecedores/lista");
        mv.addObject("listaFornecedores", fornecedorService.listar());
        return mv;
    }

    @GetMapping("/editarFornecedor/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        return cadastrar(fornecedor);
    }

    @GetMapping("/removerFornecedor/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Fornecedor fornecedor = fornecedorService.buscarPorId(id);
        fornecedorService.deletar(fornecedor);
        return listar();
    }

    @PostMapping("/salvarFornecedor")
    public ModelAndView salvar(Fornecedor fornecedor, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(fornecedor);
        }

        fornecedorService.salvar(fornecedor);
        return cadastrar(new Fornecedor());
    }
}
