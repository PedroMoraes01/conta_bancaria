package com.senai.conta_bancaria.domain.exceptions;


public class AutenticacaoIoTExpiradaException extends RuntimeException {

    // Este construtor VAZIO é obrigatório para usar "::new"
    public AutenticacaoIoTExpiradaException() {
        super("Autenticação biométrica IoT não encontrada ou expirada.");
    }

    // Opcional: construtor com mensagem personalizada
    public AutenticacaoIoTExpiradaException(String message) {
        super(message);
    }
}
