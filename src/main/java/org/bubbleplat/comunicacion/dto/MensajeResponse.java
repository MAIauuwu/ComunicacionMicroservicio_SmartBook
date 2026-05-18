package org.bubbleplat.comunicacion.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensajeResponse {

    private Long id;
    private String remitente;
    private String destinatario;
    private String contenido;
    private String asunto;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
