package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.exceptions.ValidacaoException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Double preco;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public void validar() {
        if (this.getPreco() < 50)
            throw new ValidacaoException("Preço mínimo do serviço deve ser R$ 50,00");

        long dias = ChronoUnit.DAYS.between(this.getDataInicio(), this.getDataFim());
        if (dias > 30)
            throw new ValidacaoException("Duração do serviço não pode exceder 30 dias");
    }
}
