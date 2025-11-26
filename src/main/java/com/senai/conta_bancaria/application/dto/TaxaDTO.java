package com.senai.conta_bancaria.application.dto;

public record TaxaDTO(

        @NotBlank
        @Schema(description = "Nome da taxa", example = "IOF")
        String descricao,

        @Schema(description = "Percentual da taxa", example = "0.01")
        BigDecimal percentual,

        @Schema(description = "Valor fixo da taxa", example = "2.50")
        BigDecimal valorFixo,

        @NotNull(message = "O tipo da taxa é obrigatório")
        @Schema(description = "Tipo de operação onde a taxa aplica", example = "PAGAMENTO")
        TipoTaxa tipo
) {
    public Taxa toEntity() {
        return Taxa.builder()
                .descricao(this.descricao)
                .percentual(this.percentual)
                .valorFixo(this.valorFixo)
                .tipo(this.tipo) // Mapeando o novo campo
                .build();
    }
}
