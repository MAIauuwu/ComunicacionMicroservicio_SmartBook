import React, { useState, useEffect } from 'react';
import { mensajeService } from '../services/mensajeService';
import type { MensajeResponse } from '../types/mensaje';
import './MensajeList.css';

interface Props {
  refresh: boolean;
  onRefreshComplete: () => void;
}

const MensajeList: React.FC<Props> = ({ refresh, onRefreshComplete }) => {
  const [mensajes, setMensajes] = useState<MensajeResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const [filtro, setFiltro] = useState<string>('');

  const cargarMensajes = async () => {
    setLoading(true);
    setError('');
    try {
      let data: MensajeResponse[];
      if (filtro.trim()) {
        data = await mensajeService.obtenerPorDestinatario(filtro.trim());
      } else {
        data = await mensajeService.obtenerTodos();
      }
      setMensajes(data);
    } catch {
      setError('Error al cargar los mensajes');
    } finally {
      setLoading(false);
      onRefreshComplete();
    }
  };

  useEffect(() => {
    cargarMensajes();
  }, [refresh]);

  const handleMarcarLeido = async (id: number) => {
    try {
      await mensajeService.marcarComoLeido(id);
      cargarMensajes();
    } catch {
      setError('Error al marcar como leido');
    }
  };

  const handleEliminar = async (id: number) => {
    if (!window.confirm('¿Estas seguro de eliminar este mensaje?')) return;
    try {
      await mensajeService.eliminarMensaje(id);
      cargarMensajes();
    } catch {
      setError('Error al eliminar el mensaje');
    }
  };

  const getEstadoColor = (estado: string): string => {
    switch (estado) {
      case 'PENDIENTE': return 'estado-pendiente';
      case 'ENVIADO': return 'estado-enviado';
      case 'LEIDO': return 'estado-leido';
      case 'ELIMINADO': return 'estado-eliminado';
      default: return '';
    }
  };

  const formatDate = (dateStr: string): string => {
    return new Date(dateStr).toLocaleString('es-ES');
  };

  return (
    <div className="mensaje-list">
      <h2>Mensajes</h2>

      <div className="filtro-container">
        <input
          type="email"
          placeholder="Filtrar por destinatario..."
          value={filtro}
          onChange={(e) => setFiltro(e.target.value)}
          className="filtro-input"
        />
        <button onClick={cargarMensajes} className="btn-secondary">Buscar</button>
      </div>

      {error && <div className="error-message">{error}</div>}

      {loading ? (
        <p className="loading">Cargando mensajes...</p>
      ) : mensajes.length === 0 ? (
        <p className="empty">No hay mensajes</p>
      ) : (
        <div className="mensajes-grid">
          {mensajes.map((mensaje) => (
            <div key={mensaje.id} className="mensaje-card">
              <div className="mensaje-header">
                <span className={`estado-badge ${getEstadoColor(mensaje.estado)}`}>
                  {mensaje.estado}
                </span>
                <span className="fecha">{formatDate(mensaje.fechaCreacion)}</span>
              </div>

              {mensaje.asunto && <h3 className="asunto">{mensaje.asunto}</h3>}

              <p className="contenido">{mensaje.contenido}</p>

              <div className="mensaje-info">
                <span><strong>De:</strong> {mensaje.remitente}</span>
                <span><strong>Para:</strong> {mensaje.destinatario}</span>
              </div>

              <div className="mensaje-actions">
                {mensaje.estado !== 'LEIDO' && mensaje.estado !== 'ELIMINADO' && (
                  <button
                    onClick={() => handleMarcarLeido(mensaje.id)}
                    className="btn-small btn-leido"
                  >
                    Marcar leido
                  </button>
                )}
                {mensaje.estado !== 'ELIMINADO' && (
                  <button
                    onClick={() => handleEliminar(mensaje.id)}
                    className="btn-small btn-eliminar"
                  >
                    Eliminar
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MensajeList;
