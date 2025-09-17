package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clienteDTO.toEntity();
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteDTO.fromEntity(clienteSalvo);
    }

    public List<ClienteDTO> buscarTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> new ClienteDTO(cliente.getNome(), cliente.getCpf()))
                .toList();
    }

    public Optional<ClienteDTO> buscarClientePorId(String id) {
        return clienteRepository.findById(id)
                .map(cliente -> new ClienteDTO(cliente.getNome(), cliente.getCpf()));
    }

    public void deletarCliente(String id) {
        clienteRepository.deleteById(id);
    }
}
