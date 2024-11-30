package com.projeto.sistema.controller;

import com.projeto.sistema.model.Cidade;
import com.projeto.sistema.service.CidadeService;
import com.projeto.sistema.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CidadeController {
    @Autowired
    private CidadeService cidadeService;
    @Autowired
    private EstadoService estadoService;

    @GetMapping("/cadastroCidade")
    public ModelAndView cadastrar(Cidade cidade) {
        ModelAndView mv = new ModelAndView("administrativo/cidades/cadastro");
        mv.addObject("cidade", cidade);
        mv.addObject("listaEstados", estadoService.listar());
        return mv;
    }

    @GetMapping("/listarCidade")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/cidades/lista");
        mv.addObject("listaCidades", cidadeService.listar());
        return mv;
    }

    @GetMapping("/editarCidade/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Cidade cidade = cidadeService.buscarPorId(id);
        return cadastrar(cidade);
    }

    @GetMapping("/removerCidade/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Cidade cidade = cidadeService.buscarPorId(id);
        cidadeService.deletar(cidade);
        return listar();
    }

    @PostMapping("/salvarCidade")
    public ModelAndView salvar(Cidade cidade, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(cidade);
        }

        cidadeService.salvar(cidade);
        return cadastrar(new Cidade());
    }
}
