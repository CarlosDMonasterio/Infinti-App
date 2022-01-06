import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ForgotPasswordComponent} from './modal-edit-users.component';

describe('ModalEditUsersComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ModalEditUsersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ModalEditUsersComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalEditUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
