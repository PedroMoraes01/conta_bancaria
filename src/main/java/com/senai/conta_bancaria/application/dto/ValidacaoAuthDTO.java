package com.senai.conta_bancaria.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoAuthDTO {
    private String clienteId;
    private String codigoGerado;
}
