package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.exceptions.ContaMesmoTipoException;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteDTO dto) {

        var cliente = clienteRepository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> clienteRepository.save(dto.toEntity())
        );
        cliente.setSenha(encoder.encode(dto.senha()));
        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        boolean jaTemTipo = contas.stream()
                .anyMatch(c -> c.getClass().equals(novaConta.getClass()) && c.isAtiva());
        if (jaTemTipo)
            throw new ContaMesmoTipoException();

        cliente.getContas().add(novaConta);


        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ClienteResponseDTO> listarClientesAtivos() {
        return clienteRepository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ClienteResponseDTO buscarClienteAtivoPorCpf(String cpf) {
        var cliente = buscarPorCpfClienteAtivo(cpf);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteDTO dto) {
        var cliente = clienteRepository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Cliente")
        );
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setSenha(encoder.encode(dto.senha()));

        return ClienteResponseDTO.fromEntity(clienteRepository.save(cliente));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deletarCliente(String cpf) {
        var cliente = clienteRepository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Cliente")
        );
        cliente.setAtivo(false);
        cliente.getContas().forEach(
                conta -> conta.setAtiva(false)
        );
        clienteRepository.save(cliente);
    }

    private Cliente buscarPorCpfClienteAtivo(String cpf) {
        return clienteRepository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Cliente")
        );
    }
}

