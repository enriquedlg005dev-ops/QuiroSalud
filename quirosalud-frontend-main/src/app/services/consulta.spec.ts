import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { ConsultaService } from './consulta';

describe('Consulta', () => {
  let service: ConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({providers: [provideHttpClient(), provideHttpClientTesting()]});
    service = TestBed.inject(ConsultaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
