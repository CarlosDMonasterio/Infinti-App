import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LookupPatientComponent} from './lookup-patient.component';

describe('LookupPatientComponent', () => {
  let component: LookupPatientComponent;
  let fixture: ComponentFixture<LookupPatientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LookupPatientComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LookupPatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
