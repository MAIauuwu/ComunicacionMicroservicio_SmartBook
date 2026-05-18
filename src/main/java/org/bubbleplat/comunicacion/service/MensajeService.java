package org.bubbleplat.comunicacion.service;

import lombok.RequiredArgsConstructor;
import org.bubbleplat.comunicacion.dto.MensajeRequest;
import org.bubbleplat.comunicacion.dto.MensajeResponse;
import org.bubbleplat.comunicacion.entity.Mensaje;
import org.bubbleplat.comunicacion.exception.RecursoNoEncontradoException;
import org.bubbleplat.comunicacion.exception.ReglaNegocioException;
import org.bubbleplat.comunicacion.repository.MensajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    @Transactional
    public MensajeResponse crearMensaje(MensajeRequest request) {
        validarReglasNegocio(request);

        Mensaje mensaje = Mensaje.builder()
                .remitente(request.getRemitente().toLowerCase().trim())
                .destinatario(request.getDestinatario().toLowerCase().trim())
                .contenido(request.getContenido().trim())
                .asunto(request.getAsunto() != null ? request.getAsunto().trim() : null)
                .estado(Mensaje.EstadoMensaje.PENDIENTE)
                .build();

        Mensaje guardado = mensajeRepository.save(mensaje);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public MensajeResponse obtenerMensajePorId(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Mensaje con ID " + id + " no encontrado"));

        if (mensaje.getEstado() == Mensaje.EstadoMensaje.ELIMINADO) {
            throw new RecursoNoEncontradoException(
                    "El mensaje solicitado ha sido eliminado");
        }

        return toResponse(mensaje);
    }

    @Transactional(readOnly = true)
    public List<MensajeResponse> obtenerMensajesPorRemitente(String remitente) {
        return mensajeRepository.findByRemitente(remitente.toLowerCase().trim())
                .stream()
                .filter(m -> m.getEstado() != Mensaje.EstadoMensaje.ELIMINADO)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MensajeResponse> obtenerMensajesPorDestinatario(String destinatario) {
        return mensajeRepository.findByDestinatario(destinatario.toLowerCase().trim())
                .stream()
                .filter(m -> m.getEstado() != Mensaje.EstadoMensaje.ELIMINADO)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MensajeResponse> obtenerTodosLosMensajes() {
        return mensajeRepository.findAll()
                .stream()
                .filter(m -> m.getEstado() != Mensaje.EstadoMensaje.ELIMINADO)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MensajeResponse marcarComoLeido(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Mensaje con ID " + id + " no encontrado"));

        if (mensaje.getEstado() == Mensaje.EstadoMensaje.ELIMINADO) {
            throw new ReglaNegocioException("No se puede marcar como leido un mensaje eliminado");
        }

        mensaje.setEstado(Mensaje.EstadoMensaje.LEIDO);
        Mensaje actualizado = mensajeRepository.save(mensaje);
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminarMensaje(Long id) {
        Mensaje mensaje = mensajeRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Mensaje con ID " + id + " no encontrado"));

        if (mensaje.getEstado() == Mensaje.EstadoMensaje.ELIMINADO) {
            throw new ReglaNegocioException("El mensaje ya se encuentra eliminado");
        }

        mensaje.setEstado(Mensaje.EstadoMensaje.ELIMINADO);
        mensajeRepository.save(mensaje);
    }

    private void validarReglasNegocio(MensajeRequest request) {
        if (request.getRemitente().equalsIgnoreCase(request.getDestinatario())) {
            throw new ReglaNegocioException(
                    "El remitente y destinatario no pueden ser el mismo");
        }

        String contenidoLimpio = request.getContenido().trim();
        if (contenidoLimpio.length() < 3) {
            throw new ReglaNegocioException(
                    "El contenido del mensaje debe tener al menos 3 caracteres");
        }

        if (contieneSoloEspacios(contenidoLimpio)) {
            throw new ReglaNegocioException(
                    "El contenido no puede contener solo espacios");
        }
    }

    private boolean contieneSoloEspacios(String texto) {
        return texto.matches("^\\s+$");
    }

    private MensajeResponse toResponse(Mensaje mensaje) {
        return MensajeResponse.builder()
                .id(mensaje.getId())
                .remitente(mensaje.getRemitente())
                .destinatario(mensaje.getDestinatario())
                .contenido(mensaje.getContenido())
                .asunto(mensaje.getAsunto())
                .estado(mensaje.getEstado().name())
                .fechaCreacion(mensaje.getFechaCreacion())
                .fechaActualizacion(mensaje.getFechaActualizacion())
                .build();
    }
}
