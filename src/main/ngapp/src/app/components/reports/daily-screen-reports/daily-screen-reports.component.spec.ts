import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DailyScreenReportsComponent} from './daily-screen-reports.component';

describe('DailyScreenReportsComponent', () => {
  let component: DailyScreenReportsComponent;
  let fixture: ComponentFixture<DailyScreenReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DailyScreenReportsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DailyScreenReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
