package com.projeto.sistema.repository;

import com.projeto.sistema.model.ItemEntrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemEntradaRepository extends JpaRepository<ItemEntrada, Long> {
    @Query("SELECT e FROM ItemEntrada e WHERE e.entrada.id = ?1")
    List<ItemEntrada> buscarPorEntrada(Long id);
}
