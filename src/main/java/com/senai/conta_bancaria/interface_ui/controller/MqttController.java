package com.senai.conta_bancaria.interface_ui.controller;

import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPayload;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttPublisher;
import com.rafaelcosta.spring_mqttx.domain.annotation.MqttSubscriber;
import com.senai.conta_bancaria.application.dto.SolicitacaoAuthDTO;
import com.senai.conta_bancaria.application.dto.ValidacaoAuthDTO;
import com.senai.conta_bancaria.application.service.IoTService;
import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/iot")
@RequiredArgsConstructor
@Tag(name = "Autenticação IoT", description = "Integração com dispositivos biométricos via MQTT")
public class MqttController {

    private final IoTService ioTService;
    private final ClienteRepository clienteRepository;

    // ==========================================================
    // 1. Publicador (Backend -> Broker -> Dispositivo)
    // ==========================================================

    /**
     * Endpoint chamado pelo Front-end para iniciar o processo.
     * Ao ser chamado, o retorno deste método é AUTOMATICAMENTE publicado
     * no tópico definido na anotação @MqttPublisher.
     * * Adaptacao: Como a anotação exige um tópico fixo, enviamos para "banco/autenticacao"
     * e colocamos o ID do cliente dentro do payload para o dispositivo saber quem é.
     */
    @PostMapping("/solicitar/{cpf}")
    @MqttPublisher("banco/autenticacao") // Tópico fixo configurado na anotação da lib customizada
    @Operation(summary = "Solicitar autenticação biométrica", description = "Envia comando MQTT para o dispositivo do cliente solicitar a digital.")
    public SolicitacaoAuthDTO solicitarAutenticacao(@PathVariable String cpf) {
        Cliente cliente = clienteRepository.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente"));

        // O objeto retornado aqui será serializado para JSON e enviado via MQTT
        return SolicitacaoAuthDTO.builder()
                .clienteId(cliente.getId())
                .mensagem("SOLICITAR_BIOMETRIA")
                .build();
    }

    // ==========================================================
    // 2. Assinante (Dispositivo -> Broker -> Backend)
    // ==========================================================

    /**
     * Método que fica escutando o tópico. Quando o dispositivo valida a digital,
     * ele publica nesse tópico e este método é disparado automaticamente.
     */
    @MqttSubscriber("banco/validacao") // Tópico onde os dispositivos enviam respostas
    public void receberConfirmacaoBiometria(@MqttPayload ValidacaoAuthDTO dto) {
        System.out.println("Payload recebido via MQTT: " + dto);
        ioTService.processarCodigoRecebido(dto);
    }
}
