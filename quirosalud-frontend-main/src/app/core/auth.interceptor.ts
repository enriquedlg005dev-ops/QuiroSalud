import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Nunca enviar el token a servidores externos ni a la ruta de login.
  if (!req.url.startsWith('/api/v1/') || req.url === '/api/v1/auth/login') {
    return next(req);
  }
  const token = inject(AuthService).token();
  return next(token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req);
};
