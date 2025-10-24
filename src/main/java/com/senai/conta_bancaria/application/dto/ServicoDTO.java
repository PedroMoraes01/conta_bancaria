package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Servico;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ServicoDTO(
        @Schema(description = "ID do serviço", example = "1")
        Long id,
        @NotBlank(message = "Descrição é obrigatória")
        @Schema(description = "Descrição do serviço", example = "Troca de óleo sintético")
        String descricao,

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser positivo")
        @Schema(description = "Preço do serviço", example = "120.00")
        Double preco,

        @NotNull(message = "Data de início é obrigatória")
        @Schema(description = "Data de início", example = "2025-08-05")
        LocalDate dataInicio,

        @NotNull(message = "Data de fim é obrigatória")
        @Schema(description = "Data de fim", example = "2025-08-05")
        LocalDate dataFim
) {
    public static ServicoDTO fromEntity(Servico s) {
        return new ServicoDTO(
                s.getId(),
                s.getDescricao(),
                s.getPreco(),
                s.getDataInicio(),
                s.getDataFim()
        );
    }

    public Servico toEntity() {
        return Servico.builder()
                .id(id)
                .descricao(descricao)
                .preco(preco)
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .build();
    }
}
