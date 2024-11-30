package com.projeto.sistema.service;

import com.projeto.sistema.model.Entrada;
import com.projeto.sistema.model.ItemEntrada;
import com.projeto.sistema.repository.EntradaRepository;
import com.projeto.sistema.repository.ItemEntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntradaService {

    @Autowired
    private EntradaRepository entradaRepository;
    @Autowired
    private ItemEntradaRepository itemEntradaRepository;

    public List<Entrada> listar() {
        return entradaRepository.findAll();
    }

    public Entrada buscarPorId(Long id) {
        if (entradaRepository.findById(id).isPresent()) {
            return entradaRepository.findById(id).get();
        }

        return null;
    }

    public void salvar(Entrada entrada) {
        entradaRepository.saveAndFlush(entrada);
    }

    public void deletar(Entrada entrada) {
        entradaRepository.delete(entrada);
    }

    public void deletarPorId(Long id) {
        entradaRepository.deleteById(id);
    }


    public List<ItemEntrada> darBaixaNoEstoqueAoDeletarEntrada(Entrada entrada) {
        List<ItemEntrada> listaItemEntrada = itemEntradaRepository.findAll();

        for (ItemEntrada it : listaItemEntrada) {
            if (it.getEntrada().getId().equals(entrada.getId())) {
                it.getProduto().setEstoque(it.getProduto().getEstoque() - it.getQuantidade());
                itemEntradaRepository.deleteById(it.getId());
            }
        }

        int tamanhoListaItemEntrada = listaItemEntrada.size();

        for (int i = 0; i < tamanhoListaItemEntrada - 1; i++) {
            ItemEntrada listaItemEntradaDeletados = listaItemEntrada.remove(i);
        }

        return listaItemEntrada;
    }
}
