package com.senai.conta_bancaria.interface_ui.controller;

import com.senai.conta_bancaria.application.dto.ClienteDTO;
import com.senai.conta_bancaria.application.service.ClienteService;
import com.senai.conta_bancaria.domain.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> criarCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO novoCliente = clienteService.criarCliente(clienteDTO);
        return ResponseEntity.ok(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> buscarTodosClientes() {
        List<ClienteDTO> clientes = clienteService.buscarTodosClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable String id) {
        Optional<ClienteDTO> cliente = clienteService.buscarClientePorId(id);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
