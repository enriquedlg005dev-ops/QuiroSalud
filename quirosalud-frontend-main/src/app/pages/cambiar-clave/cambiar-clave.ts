import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-cambiar-clave',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './cambiar-clave.html'
})
export class CambiarClaveComponent {
  claveActual = '';
  claveNueva = '';
  confirmacion = '';
  procesando = false;
  error = '';
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  guardar(): void {
    this.error = '';
    if (this.procesando) return;
    if (this.claveNueva.length < 10 || this.claveNueva.length > 128) {
      this.error = 'La nueva contraseña debe contener entre 10 y 128 caracteres.';
      return;
    }
    if (this.claveNueva !== this.confirmacion) {
      this.error = 'Las contraseñas nuevas no coinciden.';
      return;
    }
    if (this.claveActual === this.claveNueva) {
      this.error = 'La nueva contraseña debe ser diferente de la anterior.';
      return;
    }
    this.procesando = true;
    this.auth.cambiarClave(this.claveActual, this.claveNueva)
      .pipe(finalize(() => this.procesando = false))
      .subscribe({
        next: () => {
          this.auth.cerrarSesion();
          this.router.navigate(['/login']);
          window.alert('Contraseña actualizada. Inicia sesión con la nueva contraseña.');
        },
        error: (err) => {
          this.error = err.status === 400
            ? 'No se pudo cambiar la contraseña. Verifica la contraseña actual y la nueva.'
            : err.status === 401 || err.status === 403
              ? 'Tu sesión ya no es válida. Inicia sesión nuevamente.'
              : 'No se pudo contactar con el servicio de autenticación.';
        }
      });
  }
}
