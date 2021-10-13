import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ModalCreateSchoolComponent} from './modal-create-school.component';

describe('ModalCreateSchoolComponent', () => {
  let component: ModalCreateSchoolComponent;
  let fixture: ComponentFixture<ModalCreateSchoolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalCreateSchoolComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalCreateSchoolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
