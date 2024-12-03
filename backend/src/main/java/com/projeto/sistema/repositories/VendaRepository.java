package com.projeto.sistema.repositories;

import com.projeto.sistema.models.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<Venda, Long> {
}
