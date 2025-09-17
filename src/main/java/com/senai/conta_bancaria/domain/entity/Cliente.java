package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "cliente",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cliente_cpf", columnNames = {"cpf"})
        }
)
public class Cliente {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @NotBlank(message = "Nome é obrigatorio!! ")
    @Column(nullable = false, length = 120)
    private String nome;


    @NotBlank(message = "CPF é obrigatorio!! ")
    @Column(nullable = false, length = 11)
    private Long cpf;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Conta> contas;


    @Column(nullable = false)
    private boolean ativo;




}
