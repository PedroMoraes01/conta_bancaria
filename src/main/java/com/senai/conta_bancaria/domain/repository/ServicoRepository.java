package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
