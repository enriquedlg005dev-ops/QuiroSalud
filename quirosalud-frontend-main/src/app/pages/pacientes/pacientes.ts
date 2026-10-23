import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PacienteService, Paciente, PacienteRequest, HistoriaClinica } from '../../services/paciente';
import { AuthService } from '../../services/auth';
import Swal from 'sweetalert2';

function historiaVacia(): HistoriaClinica {
  return { motivoConsulta: '', escalaDolor: 0, antecedenteTrauma: '', antecedenteCirugia: '',
    tieneAsma: false, tieneDiabetes: false, tieneAlergias: false, observaciones: '' };
}

@Component({
  selector: 'app-pacientes',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './pacientes.html',
  styleUrl: './pacientes.css'
})
export class PacientesComponent implements OnInit {
  private readonly pacientesApi = inject(PacienteService);
  private readonly auth = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);

  listaPacientes: Paciente[] = [];
  cargando = false;
  guardando = false;
  cargandoHistoria = false;
  errorHistoria = false;
  errorListado = '';
  esEdicion = false;
  pacienteObj: Paciente & { historia: HistoriaClinica } = this.obtenerPacienteVacio();
  // Catálogo temporal; se sustituirá por una API de catálogos.
  readonly listaDistritos = [
    { codDistrito: 'D01', nombreDistrito: 'San Juan de Lurigancho' },
    { codDistrito: 'D02', nombreDistrito: 'Miraflores' },
    { codDistrito: 'D03', nombreDistrito: 'San Isidro' },
    { codDistrito: 'D04', nombreDistrito: 'Los Olivos' },
    { codDistrito: 'D05', nombreDistrito: 'Ate' },
    { codDistrito: 'D06', nombreDistrito: 'La Molina' },
    { codDistrito: 'D07', nombreDistrito: 'Surco' },
    { codDistrito: 'D08', nombreDistrito: 'San Borja' },
    { codDistrito: 'D09', nombreDistrito: 'Lince' },
    { codDistrito: 'D10', nombreDistrito: 'Comas' }
  ];

  get puedeEditarHistoria(): boolean { return this.auth.tieneRol(['ADMIN', 'MEDICO']); }
  get puedeEliminar(): boolean { return this.auth.tieneRol(['ADMIN']); }

  ngOnInit(): void { this.cargarPacientes(); }

  obtenerPacienteVacio(): Paciente & { historia: HistoriaClinica } {
    return { dni: '', nombres: '', apellidos: '', fechaNacimiento: '', sexo: 'M',
      estadoCivil: 'S', codDistrito: 'D01', direccion: '', telefono: '', ocupacion: '',
      numHijos: 0, email: '', historia: historiaVacia() };
  }

  nombreDistrito(codigo: string): string {
    return this.listaDistritos.find(d => d.codDistrito === codigo)?.nombreDistrito ?? codigo;
  }

  prepararNuevoPaciente(): void {
    this.esEdicion = false;
    this.pacienteObj = this.obtenerPacienteVacio();
    this.cargandoHistoria = false;
    this.errorHistoria = false;
  }

  abrirModalEditar(paciente: Paciente): void {
    this.esEdicion = true;
    this.pacienteObj = { ...paciente, historia: historiaVacia() };
    this.errorHistoria = false;
    this.cargandoHistoria = this.puedeEditarHistoria;
    if (this.puedeEditarHistoria) {
      this.pacientesApi.obtenerHistoria(paciente.dni).subscribe({
        next: historia => {
          if (!this.esEdicion || this.pacienteObj.dni !== paciente.dni) return;
          this.pacienteObj.historia = historia;
          this.cargandoHistoria = false;
          this.cdr.detectChanges();
        },
        error: err => {
          if (!this.esEdicion || this.pacienteObj.dni !== paciente.dni) return;
          this.cargandoHistoria = false;
          if (err.status !== 404) {
            this.errorHistoria = true;
            Swal.fire('Error', 'No se pudo recuperar la historia clínica. No guardes hasta reabrir el registro.', 'error');
          }
          this.cdr.detectChanges();
        }
      });
    }
  }

  cargarPacientes(): void {
    this.cargando = true;
    this.errorListado = '';
    this.pacientesApi.listarPacientes().subscribe({
      next: datos => { this.listaPacientes = datos; this.cargando = false; this.cdr.detectChanges(); },
      error: err => { this.cargando = false; this.errorListado = `No se pudieron consultar los pacientes (HTTP ${err.status}).`; this.cdr.detectChanges(); }
    });
  }

  guardar(): void {
    if (this.guardando || this.cargandoHistoria || this.errorHistoria) return;
    const base = { ...this.pacienteObj, email: this.pacienteObj.email?.trim() || null };
    const datos: PacienteRequest = this.puedeEditarHistoria ? base : { ...base, historia: undefined };
    this.guardando = true;
    const solicitud = this.esEdicion
      ? this.pacientesApi.actualizarPaciente(datos.dni, datos)
      : this.pacientesApi.registrarPaciente(datos);
    solicitud.subscribe({
      next: () => {
        this.guardando = false;
        document.querySelector<HTMLButtonElement>('#modalPaciente .btn-close')?.click();
        Swal.fire('Operación exitosa', this.esEdicion ? 'Paciente actualizado.' : 'Paciente registrado.', 'success');
        this.pacienteObj = this.obtenerPacienteVacio();
        this.cargarPacientes();
      },
      error: err => {
        this.guardando = false;
        const mensaje = err.error?.message || (err.status === 409 ? 'El DNI ya se encuentra registrado.' : 'Verifica los datos ingresados.');
        Swal.fire('No se pudo guardar', mensaje, 'error');
      }
    });
  }

  permitirSoloNumeros(event: KeyboardEvent): void {
    if (event.key.length === 1 && !/[0-9]/.test(event.key)) event.preventDefault();
  }

  eliminar(paciente: Paciente): void {
    if (!this.puedeEliminar) return;
    Swal.fire({title: '¿Eliminar paciente?', text: `Se eliminará a ${paciente.nombres} ${paciente.apellidos}.`,
      icon: 'warning', showCancelButton: true, confirmButtonText: 'Eliminar', cancelButtonText: 'Cancelar'
    }).then(confirmacion => {
      if (!confirmacion.isConfirmed) return;
      this.pacientesApi.eliminarPaciente(paciente.dni).subscribe({
        next: () => { Swal.fire('Eliminado', 'Paciente eliminado correctamente.', 'success'); this.cargarPacientes(); },
        error: () => Swal.fire('Error', 'No se pudo eliminar el paciente.', 'error')
      });
    });
  }
}
