import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HygieneReportsComponent} from './hygiene-reports.component';

describe('HygieneReportsComponent', () => {
  let component: HygieneReportsComponent;
  let fixture: ComponentFixture<HygieneReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HygieneReportsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HygieneReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
