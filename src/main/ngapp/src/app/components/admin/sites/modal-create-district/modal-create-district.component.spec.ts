import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ModalCreateDistrictComponent} from './modal-create-district.component';

describe('ModalCreateSiteComponent', () => {
  let component: ModalCreateDistrictComponent;
  let fixture: ComponentFixture<ModalCreateDistrictComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalCreateDistrictComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalCreateDistrictComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
