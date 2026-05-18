package org.bubbleplat.comunicacion.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bubbleplat.comunicacion.dto.MensajeRequest;
import org.bubbleplat.comunicacion.dto.MensajeResponse;
import org.bubbleplat.comunicacion.service.MensajeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@RequiredArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;

    @PostMapping
    public ResponseEntity<MensajeResponse> crearMensaje(@Valid @RequestBody MensajeRequest request) {
        MensajeResponse response = mensajeService.crearMensaje(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeResponse> obtenerMensaje(@PathVariable Long id) {
        MensajeResponse response = mensajeService.obtenerMensajePorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MensajeResponse>> obtenerTodosLosMensajes() {
        List<MensajeResponse> mensajes = mensajeService.obtenerTodosLosMensajes();
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/remitente/{email}")
    public ResponseEntity<List<MensajeResponse>> obtenerPorRemitente(@PathVariable String email) {
        List<MensajeResponse> mensajes = mensajeService.obtenerMensajesPorRemitente(email);
        return ResponseEntity.ok(mensajes);
    }

    @GetMapping("/destinatario/{email}")
    public ResponseEntity<List<MensajeResponse>> obtenerPorDestinatario(@PathVariable String email) {
        List<MensajeResponse> mensajes = mensajeService.obtenerMensajesPorDestinatario(email);
        return ResponseEntity.ok(mensajes);
    }

    @PatchMapping("/{id}/leido")
    public ResponseEntity<MensajeResponse> marcarComoLeido(@PathVariable Long id) {
        MensajeResponse response = mensajeService.marcarComoLeido(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        mensajeService.eliminarMensaje(id);
        return ResponseEntity.noContent().build();
    }
}
