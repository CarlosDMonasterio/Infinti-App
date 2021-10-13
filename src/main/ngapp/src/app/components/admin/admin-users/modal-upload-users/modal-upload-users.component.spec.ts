import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ModalUploadUsersComponent} from './modal-upload-users.component';

describe('ModalUploadUsersComponent', () => {
  let component: ModalUploadUsersComponent;
  let fixture: ComponentFixture<ModalUploadUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalUploadUsersComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalUploadUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
