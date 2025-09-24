package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository clienteRepository;

    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteDTO dto) {

        var cliente = clienteRepository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> clienteRepository.save(dto.toEntity())
        );

        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        boolean jaTemTipo = contas.stream()
                .anyMatch(c -> c.getClass().equals(novaConta.getClass()) && c.isAtiva());
        if (jaTemTipo)
            throw new RuntimeException("Cliente ja possui uma conta ativa desse tipo");

        cliente.getContas().add(novaConta);





        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    public List<ClienteResponseDTO> listarClientesAtivos() {
        return clienteRepository.findAllByAtivotrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }
}
