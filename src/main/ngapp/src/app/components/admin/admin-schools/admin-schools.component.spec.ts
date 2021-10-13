import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AdminSchoolsComponent} from './admin-schools.component';

describe('AdminSchoolsComponent', () => {
  let component: AdminSchoolsComponent;
  let fixture: ComponentFixture<AdminSchoolsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminSchoolsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSchoolsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
