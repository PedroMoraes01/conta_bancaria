package com.senai.conta_bancaria.application.service;

import com.senai.conta_bancaria.application.dto.ValidacaoAuthDTO;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;
import com.senai.conta_bancaria.domain.exceptions.AutenticacaoIoTExpiradaException;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.CodigoAutenticacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IoTService {

    private final CodigoAutenticacaoRepository codigoRepository;
    private final ClienteRepository clienteRepository;

    // Chamado pelo PagamentoAppService antes de confirmar o pagamento
    public void validarCodigoBiometrico(String clienteId) {
        CodigoAutenticacao auth = codigoRepository.findTopByClienteIdAndValidadoFalseOrderByExpiraEmDesc(clienteId)
                .orElseThrow(AutenticacaoIoTExpiradaException::new);

        if (auth.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new AutenticacaoIoTExpiradaException();
        }

        auth.setValidado(true); // Consome o código
        codigoRepository.save(auth);
    }

    // Chamado pelo MqttController quando chega mensagem do dispositivo
    @Transactional
    public void processarCodigoRecebido(ValidacaoAuthDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente IoT"));

        CodigoAutenticacao codigo = CodigoAutenticacao.builder()
                .cliente(cliente)
                .codigo(dto.getCodigoGerado())
                .validado(false)
                .expiraEm(LocalDateTime.now().plusMinutes(2)) // Validade de 2 min
                .build();

        codigoRepository.save(codigo);
        System.out.println("Código IoT salvo para cliente: " + cliente.getNome());
    }
}
