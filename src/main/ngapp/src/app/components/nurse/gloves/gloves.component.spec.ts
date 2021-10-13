import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GlovesComponent} from './gloves.component';

describe('GlovesComponent', () => {
  let component: GlovesComponent;
  let fixture: ComponentFixture<GlovesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GlovesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GlovesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
