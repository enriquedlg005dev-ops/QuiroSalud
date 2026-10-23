import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-navbar', standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html', styleUrl: './navbar.css'
})
export class NavbarComponent {
  private readonly router = inject(Router);
  private readonly auth = inject(AuthService);
  get cargoActual(): string { return this.auth.usuarioActual()?.cargo ?? ''; }
  get nombreActual(): string { return this.auth.usuarioActual()?.nombres ?? 'Usuario'; }
  cerrarSesion(): void {
    this.auth.cerrarSesion();
    this.router.navigate(['/login']);
  }
}
