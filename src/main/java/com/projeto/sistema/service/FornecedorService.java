package com.projeto.sistema.service;

import com.projeto.sistema.model.Funcionario;
import com.projeto.sistema.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    public List<Funcionario> listar() {
        return funcionarioRepository.findAll();
    }

    public Funcionario buscarPorId(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new RuntimeException("Funcionário não existe");
        }

        return funcionarioRepository.findById(id).get();
    }

    public void salvar(Funcionario funcionario) {
        funcionarioRepository.saveAndFlush(funcionario);
    }

    public void deletar(Funcionario funcionario) {
        funcionarioRepository.delete(funcionario);
    }

    public void deletarPorId(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new RuntimeException("Funcionário não existe");
        }

        funcionarioRepository.deleteById(id);
    }
}
