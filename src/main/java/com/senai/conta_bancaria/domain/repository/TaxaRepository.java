package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.TipoTaxa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxaRepository extends JpaRepository<Taxa, String> {
    // Busca taxas específicas (ex: só as de PAGAMENTO)
    List<Taxa> findByTipo(TipoTaxa tipo);
}
