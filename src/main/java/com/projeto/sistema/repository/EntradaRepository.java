package com.projeto.sistema.repository;

import com.projeto.sistema.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
}
