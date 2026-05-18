import React, { useState } from 'react';
import { mensajeService } from '../services/mensajeService';
import type { MensajeRequest } from '../types/mensaje';
import './MensajeForm.css';

interface Props {
  onMensajeCreado: () => void;
}

const MensajeForm: React.FC<Props> = ({ onMensajeCreado }) => {
  const [formData, setFormData] = useState<MensajeRequest>({
    remitente: '',
    destinatario: '',
    contenido: '',
    asunto: '',
  });
  const [error, setError] = useState<string>('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    setError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const dataToSend: MensajeRequest = {
        remitente: formData.remitente,
        destinatario: formData.destinatario,
        contenido: formData.contenido,
        asunto: formData.asunto || undefined,
      };

      await mensajeService.crearMensaje(dataToSend);
      setFormData({ remitente: '', destinatario: '', contenido: '', asunto: '' });
      onMensajeCreado();
    } catch (err: unknown) {
      if (err && typeof err === 'object' && 'response' in err) {
        const axiosError = err as { response?: { data?: { message?: string; validationErrors?: Record<string, string> } } };
        const data = axiosError.response?.data;
        if (data?.validationErrors) {
          setError(Object.values(data.validationErrors).join(', '));
        } else {
          setError(data?.message || 'Error al crear el mensaje');
        }
      } else {
        setError('Error al crear el mensaje');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="mensaje-form">
      <h2>Nuevo Mensaje</h2>

      {error && <div className="error-message">{error}</div>}

      <div className="form-group">
        <label htmlFor="remitente">Remitente (email)</label>
        <input
          type="email"
          id="remitente"
          name="remitente"
          value={formData.remitente}
          onChange={handleChange}
          required
          placeholder="usuario@ejemplo.com"
        />
      </div>

      <div className="form-group">
        <label htmlFor="destinatario">Destinatario (email)</label>
        <input
          type="email"
          id="destinatario"
          name="destinatario"
          value={formData.destinatario}
          onChange={handleChange}
          required
          placeholder="destinatario@ejemplo.com"
        />
      </div>

      <div className="form-group">
        <label htmlFor="asunto">Asunto (opcional)</label>
        <input
          type="text"
          id="asunto"
          name="asunto"
          value={formData.asunto}
          onChange={handleChange}
          placeholder="Asunto del mensaje"
        />
      </div>

      <div className="form-group">
        <label htmlFor="contenido">Contenido</label>
        <textarea
          id="contenido"
          name="contenido"
          value={formData.contenido}
          onChange={handleChange}
          required
          rows={4}
          placeholder="Escribe tu mensaje aqui..."
        />
      </div>

      <button type="submit" disabled={loading} className="btn-primary">
        {loading ? 'Enviando...' : 'Enviar Mensaje'}
      </button>
    </form>
  );
};

export default MensajeForm;
