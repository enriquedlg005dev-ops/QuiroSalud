import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE } from '../core/api';

export interface HistoriaClinica {
  motivoConsulta: string;
  escalaDolor: number;
  antecedenteTrauma: string;
  antecedenteCirugia: string;
  tieneAsma: boolean;
  tieneDiabetes: boolean;
  tieneAlergias: boolean;
  observaciones: string;
}
export interface Paciente {
  dni: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  sexo: string;
  estadoCivil: string;
  ocupacion: string;
  numHijos: number;
  telefono: string;
  email: string | null;
  direccion: string;
  codDistrito: string;
}
export interface PacienteRequest extends Paciente { historia?: HistoriaClinica; }

@Injectable({ providedIn: 'root' })
export class PacienteService {
  private readonly http = inject(HttpClient);
  private readonly url = `${API_BASE}/pacientes`;

  listarPacientes(): Observable<Paciente[]> { return this.http.get<Paciente[]>(this.url); }
  buscarPaciente(dni: string): Observable<Paciente> { return this.http.get<Paciente>(`${this.url}/${dni}`); }
  obtenerHistoria(dni: string): Observable<HistoriaClinica> {
    return this.http.get<HistoriaClinica>(`${this.url}/${dni}/historia`);
  }
  registrarPaciente(paciente: PacienteRequest): Observable<Paciente> {
    return this.http.post<Paciente>(this.url, paciente);
  }
  actualizarPaciente(dni: string, paciente: PacienteRequest): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.url}/${dni}`, paciente);
  }
  eliminarPaciente(dni: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${dni}`);
  }
}
