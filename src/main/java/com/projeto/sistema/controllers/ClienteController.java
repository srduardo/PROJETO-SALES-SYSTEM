package com.projeto.sistema.controllers;

import com.projeto.sistema.models.Cliente;
import com.projeto.sistema.services.CidadeService;
import com.projeto.sistema.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClienteController {
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private CidadeService cidadeService;

    @GetMapping("/cadastroCliente")
    public ModelAndView cadastrar(Cliente cliente) {
        ModelAndView mv = new ModelAndView("administrativo/clientes/cadastro");
        mv.addObject("cliente", cliente);
        mv.addObject("listaCidades", cidadeService.listar());
        return mv;
    }

    @GetMapping("/listarCliente")
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("/administrativo/clientes/lista");
        mv.addObject("listaClientes", clienteService.listar());
        return mv;
    }

    @GetMapping("/editarCliente/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return cadastrar(cliente);
    }

    @GetMapping("/removerCliente/{id}")
    public ModelAndView remover(@PathVariable("id") Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        clienteService.deletar(cliente);
        return listar();
    }

    @PostMapping("/salvarCliente")
    public ModelAndView salvar(Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return cadastrar(cliente);
        }

        clienteService.salvar(cliente);
        return cadastrar(new Cliente());
    }
}
