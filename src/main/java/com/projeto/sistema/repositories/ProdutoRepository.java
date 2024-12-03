package com.projeto.sistema.repositories;

import com.projeto.sistema.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
