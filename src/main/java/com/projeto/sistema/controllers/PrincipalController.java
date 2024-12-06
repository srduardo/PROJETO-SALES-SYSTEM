package com.projeto.sistema.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PrincipalController {

    @GetMapping("/")
    public String acessarPrincipal() {
        return "resources/templates/administrativo/home";
    }
}
