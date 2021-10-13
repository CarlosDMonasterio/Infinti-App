import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ModalUploadSchoolsComponent} from './modal-upload-schools.component';

describe('ModalUploadSchoolsComponent', () => {
  let component: ModalUploadSchoolsComponent;
  let fixture: ComponentFixture<ModalUploadSchoolsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalUploadSchoolsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalUploadSchoolsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
