package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ServicoDTO;
import com.senai.conta_bancaria.domain.entity.Servico;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ServicoRepository;

import java.util.List;

public class ServicoAppService {

    private final ServicoRepository repository;

    public ServicoAppService(ServicoRepository repository) {
        this.repository = repository;
    }

    public ServicoDTO salvar(ServicoDTO dto) {
        Servico servico = dto.toEntity();
        servico.validar();
        return ServicoDTO.fromEntity(repository.save(servico));
    }

    public List<ServicoDTO> listar() {
        return repository.findAll()
                .stream()
                .map(ServicoDTO::fromEntity)
                .toList();
    }

    public ServicoDTO buscarPorId(Long id) {
        return ServicoDTO.fromEntity(
                repository.findById(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço com ID " + id + " não encontrado."))
        );
    }

    public ServicoDTO atualizar(Long id, ServicoDTO dtoAtualizado) {
        Servico existente = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço com ID " + id + " não encontrado."));

        Servico atualizado = dtoAtualizado.toEntity();
        atualizado.setId(existente.getId());

        atualizado.validar();
        return ServicoDTO.fromEntity(repository.save(atualizado));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Serviço com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }
}
