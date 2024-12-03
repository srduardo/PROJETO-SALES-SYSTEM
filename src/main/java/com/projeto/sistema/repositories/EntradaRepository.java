package com.projeto.sistema.repositories;

import com.projeto.sistema.models.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
}
