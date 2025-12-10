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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/iot")
@RequiredArgsConstructor
@Tag(name = "Autenticação IoT", description = "Integração com dispositivos biométricos via MQTT")
public class MqttController {

    private final IoTService ioTService;
    private final ClienteRepository clienteRepository;

   /* @PostMapping("/solicitar/{cpf}")
    @MqttPublisher("banco/autenticacao")
    @Operation(summary = "Solicitar autenticação biométrica", description = "Envia comando MQTT para o dispositivo do cliente solicitar a digital.")
    public SolicitacaoAuthDTO solicitarAutenticacao(@PathVariable String cpf) {
        Cliente cliente = clienteRepository.findByCpfAndAtivoTrue(cpf)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente"));

        return SolicitacaoAuthDTO.builder()
                .clienteId(cliente.getId())
                .mensagem("SOLICITAR_BIOMETRIA")
                .build();
    }

    @MqttSubscriber("banco/validacao") // Tópico onde os dispositivos enviam respostas
    public void receberConfirmacaoBiometria(@MqttPayload ValidacaoAuthDTO dto) {
        System.out.println("Payload recebido via MQTT: " + dto);
        ioTService.processarCodigoRecebido(dto);
    } */

    //para facilitar os testes, teve que se implementar
    @PostMapping("/resposta")
    @Operation(
            summary = "SIMULAR - Dispositivo envia o código (TESTE)",
            description = "Endpoint para injetar o código de autenticação no sistema para fins de teste sem MQTT."
    )
    public ResponseEntity<Void> simularRespostaBiometria(@RequestBody ValidacaoAuthDTO dto) {
        ioTService.processarCodigoRecebido(dto);
        return ResponseEntity.ok().build();
    }
}
