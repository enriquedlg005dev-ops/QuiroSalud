import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { API_BASE } from '../core/api';

export interface Credenciales { email: string; clave: string; }
export interface UsuarioSesion {
  idUsuario: number;
  email: string;
  nombres: string;
  cargo: string;
  token: string;
}
interface RespuestaLogin { response: UsuarioSesion; error?: {message: string}; }

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly KEY = 'quirosalud.session';

  login(credenciales: Credenciales): Observable<RespuestaLogin> {
    return this.http.post<RespuestaLogin>(`${API_BASE}/auth/login`, credenciales).pipe(
      tap(({response}) => {
        if (!response?.token || !response.cargo) {
          throw new Error('Respuesta de autenticación incompleta');
        }
        sessionStorage.setItem(this.KEY, JSON.stringify(response));
      })
    );
  }

  cambiarClave(claveActual: string, claveNueva: string): Observable<void> {
    return this.http.post<void>(`${API_BASE}/auth/cambiar-clave`, { claveActual, claveNueva });
  }

  usuarioActual(): UsuarioSesion | null {
    try {
      const raw = sessionStorage.getItem(this.KEY);
      if (!raw) return null;
      const usuario = JSON.parse(raw) as UsuarioSesion;
      if (!usuario.token || !this.tokenNoExpirado(usuario.token)) {
        this.cerrarSesion();
        return null;
      }
      return usuario;
    } catch {
      this.cerrarSesion();
      return null;
    }
  }

  token(): string | null { return this.usuarioActual()?.token ?? null; }

  tieneRol(roles: readonly string[]): boolean {
    const usuario = this.usuarioActual();
    return !!usuario && roles.includes(usuario.cargo.trim().toUpperCase());
  }

  cerrarSesion(): void { sessionStorage.removeItem(this.KEY); }

  private tokenNoExpirado(jwt: string): boolean {
    try {
      const payload = jwt.split('.')[1];
      if (!payload) return false;
      const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
      const contenido = JSON.parse(atob(base64)) as {exp?: number};
      return typeof contenido.exp === 'number' && contenido.exp * 1000 > Date.now();
    } catch { return false; }
  }
}
