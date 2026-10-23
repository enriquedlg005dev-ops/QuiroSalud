import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-dashboard', standalone: true,
  imports: [RouterLink], templateUrl: './dashboard.html', styleUrls: ['./dashboard.css']
})
export class DashboardComponent {
  private readonly auth = inject(AuthService);
  get nombreActual(): string { return this.auth.usuarioActual()?.nombres ?? 'Usuario'; }
  get cargoActual(): string { return this.auth.usuarioActual()?.cargo ?? 'SIN CARGO'; }
}
