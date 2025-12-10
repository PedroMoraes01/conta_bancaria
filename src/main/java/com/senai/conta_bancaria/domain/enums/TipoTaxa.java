package com.senai.conta_bancaria.domain.enums;

public enum TipoTaxa {
    PAGAMENTO,      // Taxas para boletos e contas
    TRANSFERENCIA,  // Taxas para TED/DOC/Pix
    SAQUE,          // Taxas para retirada em dinheiro
    MANUTENCAO      // Taxas mensais
}
