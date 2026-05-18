package org.bubbleplat.comunicacion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MensajeRequest {

    @NotBlank(message = "El remitente es obligatorio")
    @Email(message = "El remitente debe tener formato de email valido")
    private String remitente;

    @NotBlank(message = "El destinatario es obligatorio")
    @Email(message = "El destinatario debe tener formato de email valido")
    private String destinatario;

    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 2000, message = "El contenido debe tener entre 1 y 2000 caracteres")
    private String contenido;

    @Size(max = 255, message = "El asunto no puede exceder 255 caracteres")
    private String asunto;
}
