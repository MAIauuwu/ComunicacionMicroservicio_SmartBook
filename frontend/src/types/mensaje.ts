export interface MensajeRequest {
  remitente: string;
  destinatario: string;
  contenido: string;
  asunto?: string;
}

export interface MensajeResponse {
  id: number;
  remitente: string;
  destinatario: string;
  contenido: string;
  asunto: string | null;
  estado: string;
  fechaCreacion: string;
  fechaActualizacion: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}
