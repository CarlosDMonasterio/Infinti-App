import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GloveReportsComponent} from './glove-reports.component';

describe('GloveReportsComponent', () => {
  let component: GloveReportsComponent;
  let fixture: ComponentFixture<GloveReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GloveReportsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GloveReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
