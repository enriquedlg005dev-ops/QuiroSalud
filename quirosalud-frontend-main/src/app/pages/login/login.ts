import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { AuthService, Credenciales } from '../../services/auth';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {
  credenciales: Credenciales = { email: '', clave: '' };
  ingresando = false;
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  ingresar(): void {
    if (this.ingresando) return;
    this.ingresando = true;
    this.auth.login(this.credenciales).pipe(finalize(() => this.ingresando = false)).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        const mensaje = err.status === 401
          ? 'Correo o contraseña incorrectos'
          : 'No se pudo iniciar sesión. Revisa si Auth y el Gateway están funcionando.';
        Swal.fire('Error de acceso', mensaje, 'error');
      }
    });
  }
}
