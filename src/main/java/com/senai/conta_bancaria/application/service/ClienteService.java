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
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.nome());
        cliente.setCpf(clienteDTO.cpf());
        return clienteRepository.save(cliente);
    }

    public List<Cliente> buscarTodosClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarClientePorId(String id) {
        return clienteRepository.findById(id);
    }

    public void deletarCliente(String id) {
        clienteRepository.deleteById(id);
    }
}
