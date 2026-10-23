import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { VentaService } from './venta';

describe('Venta', () => {
  let service: VentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({providers: [provideHttpClient(), provideHttpClientTesting()]});
    service = TestBed.inject(VentaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
