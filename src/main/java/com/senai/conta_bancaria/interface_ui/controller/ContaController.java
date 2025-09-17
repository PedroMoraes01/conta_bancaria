package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.service.ContaService;
import com.senai.conta_bancaria.domain.entity.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;


    @RestController
    @RequestMapping("/api/contas")
    public class ContaController {

        @Autowired
        private ContaService contaService;

        @PostMapping("/depositar")
        public ResponseEntity<Conta> depositar(@RequestBody Map<String, Object> payload) {
            String numeroConta = (String) payload.get("numeroConta");
            BigDecimal valor = (BigDecimal) payload.get("valor");
            try {
                Conta contaAtualizada = contaService.depositar(numeroConta, valor);
                return ResponseEntity.ok(contaAtualizada);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        @PostMapping("/sacar")
        public ResponseEntity<Conta> sacar(@RequestBody Map<String, Object> payload) {
            String numeroConta = (String) payload.get("numeroConta");
            BigDecimal valor = (BigDecimal) payload.get("valor");
            try {
                Conta contaAtualizada = contaService.sacar(numeroConta, valor);
                return ResponseEntity.ok(contaAtualizada);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        @PostMapping("/transferir")
        public ResponseEntity<Void> transferir(@RequestBody Map<String, Object> payload) {
            String numeroContaOrigem = (String) payload.get("numeroContaOrigem");
            String numeroContaDestino = (String) payload.get("numeroContaDestino");
            BigDecimal valor = (BigDecimal) payload.get("valor");
            try {
                contaService.transferir(numeroContaOrigem, numeroContaDestino, valor);
                return ResponseEntity.ok().build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
    }

