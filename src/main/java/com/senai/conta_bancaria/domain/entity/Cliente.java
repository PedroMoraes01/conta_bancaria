package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Cliente {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @NotBlank(message = "Nome é obrigatorio!! ")
    @Column(nullable = false, length = 100)
    private String nome;


    @NotBlank(message = "CPF é obrigatorio!! ")
    @Column(nullable = false, length = 100)
    private Long cpf;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Conta> contas;




}
