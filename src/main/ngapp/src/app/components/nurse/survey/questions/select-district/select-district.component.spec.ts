import {ComponentFixture, TestBed} from '@angular/core/testing';

import {SelectDistrictComponent} from './select-district.component';

describe('SelectDistrictComponent', () => {
  let component: SelectDistrictComponent;
  let fixture: ComponentFixture<SelectDistrictComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SelectDistrictComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectDistrictComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
