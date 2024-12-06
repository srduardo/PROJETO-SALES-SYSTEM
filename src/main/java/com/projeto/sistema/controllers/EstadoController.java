package com.projeto.sistema.controllers;

import com.projeto.sistema.models.Estado;
import com.projeto.sistema.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @GetMapping("/cadastroEstado")
    public ModelAndView cadastrar(Estado estado) {
        ModelAndView mv = new ModelAndView("administrativo/estados/cadastro");
        mv.addObject("estado", estado);
        return mv;
    }

    @GetMapping("/listarEstado")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("administrativo/estados/lista");
        mv.addObject("listaEstados", estadoService.listar());
        return mv;
    }

    @GetMapping("/editarEstado/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Estado estado = estadoService.buscarPorId(id);
        return cadastrar(estado);
    }

    @GetMapping("/removerEstado/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Estado estado = estadoService.buscarPorId(id);
        estadoService.deletar(estado);
        return listar();
    }

    @PostMapping("/salvarEstado")
    public ModelAndView salvar(Estado estado, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(estado);
        }

        estadoService.salvar(estado);
        return cadastrar(new Estado());
    }
}
