import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MiniPagerComponent} from './mini-pager.component';

describe('MiniPagerComponent', () => {
  let component: MiniPagerComponent;
  let fixture: ComponentFixture<MiniPagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MiniPagerComponent]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MiniPagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
