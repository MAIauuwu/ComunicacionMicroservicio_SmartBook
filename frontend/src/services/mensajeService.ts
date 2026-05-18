import axios from 'axios';
import type { MensajeRequest, MensajeResponse } from '../types/mensaje';

const API_URL = 'http://localhost:8081/api/mensajes';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const mensajeService = {
  crearMensaje: async (data: MensajeRequest): Promise<MensajeResponse> => {
    const response = await api.post<MensajeResponse>('', data);
    return response.data;
  },

  obtenerMensaje: async (id: number): Promise<MensajeResponse> => {
    const response = await api.get<MensajeResponse>(`/${id}`);
    return response.data;
  },

  obtenerTodos: async (): Promise<MensajeResponse[]> => {
    const response = await api.get<MensajeResponse[]>('');
    return response.data;
  },

  obtenerPorRemitente: async (email: string): Promise<MensajeResponse[]> => {
    const response = await api.get<MensajeResponse[]>(`/remitente/${email}`);
    return response.data;
  },

  obtenerPorDestinatario: async (email: string): Promise<MensajeResponse[]> => {
    const response = await api.get<MensajeResponse[]>(`/destinatario/${email}`);
    return response.data;
  },

  marcarComoLeido: async (id: number): Promise<MensajeResponse> => {
    const response = await api.patch<MensajeResponse>(`/${id}/leido`);
    return response.data;
  },

  eliminarMensaje: async (id: number): Promise<void> => {
    await api.delete(`/${id}`);
  },
};
