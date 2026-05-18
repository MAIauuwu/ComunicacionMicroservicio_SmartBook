package org.bubbleplat.comunicacion.repository;

import org.bubbleplat.comunicacion.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByRemitente(String remitente);

    List<Mensaje> findByDestinatario(String destinatario);

    List<Mensaje> findByRemitenteAndDestinatario(String remitente, String destinatario);

    boolean existsByRemitenteAndDestinatario(String remitente, String destinatario);
}
