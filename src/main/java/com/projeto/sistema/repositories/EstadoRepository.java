package com.projeto.sistema.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.sistema.models.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
