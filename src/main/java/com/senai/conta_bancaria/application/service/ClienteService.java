package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private ClienteRepository clienteRepository;

    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteDTO dto) {

        var cliente = clienteRepository.findByCpfAtivoTrue(dto.cpf()).orElseGet(
                () -> clienteRepository.save(dto.toEntity())
        );

        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        return null;
    }

}
