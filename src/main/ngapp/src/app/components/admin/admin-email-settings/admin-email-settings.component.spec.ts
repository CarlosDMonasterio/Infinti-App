import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AdminEmailSettingsComponent} from './admin-email-settings.component';

describe('AdminEmailSettingsComponent', () => {
  let component: AdminEmailSettingsComponent;
  let fixture: ComponentFixture<AdminEmailSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AdminEmailSettingsComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminEmailSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
