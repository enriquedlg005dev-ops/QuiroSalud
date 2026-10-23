import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { PacienteService } from './paciente';

describe('PacienteService', () => {
  let api: PacienteService;
  let http: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({providers: [provideHttpClient(), provideHttpClientTesting()]});
    api = TestBed.inject(PacienteService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('lista desde la ruta REST real', () => {
    api.listarPacientes().subscribe(res => expect(res).toEqual([]));
    const request = http.expectOne('/api/v1/pacientes');
    expect(request.request.method).toBe('GET');
    request.flush([]);
  });

  it('elimina usando DNI y DELETE', () => {
    api.eliminarPaciente('12345678').subscribe();
    const request = http.expectOne('/api/v1/pacientes/12345678');
    expect(request.request.method).toBe('DELETE');
    request.flush(null);
  });
});
