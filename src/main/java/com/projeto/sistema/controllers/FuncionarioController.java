package com.projeto.sistema.controllers;

import com.projeto.sistema.models.Funcionario;
import com.projeto.sistema.services.FuncionarioService;
import com.projeto.sistema.services.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FuncionarioController {
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private CidadeService cidadeService;

    @GetMapping("/cadastroFuncionario")
    public ModelAndView cadastrar(Funcionario funcionario) {
        ModelAndView mv = new ModelAndView("administrativo/funcionarios/cadastro");
        mv.addObject("funcionario", funcionario);
        mv.addObject("listaCidades", cidadeService.listar());
        return mv;
    }

    @GetMapping("/listarFuncionario")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/funcionarios/lista");
        mv.addObject("listaFuncionarios", funcionarioService.listar());
        return mv;
    }

    @GetMapping("/editarFuncionario/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return cadastrar(funcionario);
    }

    @GetMapping("/removerFuncionario/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        funcionarioService.deletar(funcionario);
        return listar();
    }

    @PostMapping("/salvarFuncionario")
    public ModelAndView salvar(Funcionario funcionario, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(funcionario);
        }

        funcionarioService.salvar(funcionario);
        return cadastrar(new Funcionario());
    }
}
