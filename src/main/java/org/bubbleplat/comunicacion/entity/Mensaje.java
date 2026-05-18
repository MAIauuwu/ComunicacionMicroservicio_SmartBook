package org.bubbleplat.comunicacion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El remitente es obligatorio")
    @Email(message = "El remitente debe tener formato de email valido")
    @Column(nullable = false)
    private String remitente;

    @NotBlank(message = "El destinatario es obligatorio")
    @Email(message = "El destinatario debe tener formato de email valido")
    @Column(nullable = false)
    private String destinatario;

    @NotBlank(message = "El contenido del mensaje es obligatorio")
    @Size(min = 1, max = 2000, message = "El contenido debe tener entre 1 y 2000 caracteres")
    @Column(nullable = false, length = 2000)
    private String contenido;

    @Size(max = 255, message = "El asunto no puede exceder 255 caracteres")
    @Column(length = 255)
    private String asunto;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMensaje estado = EstadoMensaje.PENDIENTE;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;

    public enum EstadoMensaje {
        PENDIENTE,
        ENVIADO,
        LEIDO,
        ELIMINADO
    }
}
