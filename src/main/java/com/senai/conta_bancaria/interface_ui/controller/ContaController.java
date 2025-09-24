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


    }

