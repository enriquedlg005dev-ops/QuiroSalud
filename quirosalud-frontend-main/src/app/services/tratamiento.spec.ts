import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';

import { TratamientoService } from './tratamiento';

describe('Tratamiento', () => {
  let service: TratamientoService;

  beforeEach(() => {
    TestBed.configureTestingModule({providers: [provideHttpClient(), provideHttpClientTesting()]});
    service = TestBed.inject(TratamientoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
