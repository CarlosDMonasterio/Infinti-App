import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HygieneComponent} from './hygiene.component';

describe('HygieneComponent', () => {
  let component: HygieneComponent;
  let fixture: ComponentFixture<HygieneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HygieneComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HygieneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
