import { TestBed } from '@angular/core/testing';

import { GooglekeyService } from './googlekey.service';

describe('GooglekeyService', () => {
  let service: GooglekeyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GooglekeyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
