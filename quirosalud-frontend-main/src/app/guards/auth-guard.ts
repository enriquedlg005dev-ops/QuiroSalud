import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth';

// El guard solo mejora la navegación. El servidor valida permisos realmente.
export const authGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (!auth.usuarioActual()) return router.createUrlTree(['/login']);
  const roles = route.data?.['roles'] as string[] | undefined;
  if (roles && !auth.tieneRol(roles)) return router.createUrlTree(['/dashboard']);
  return true;
};
