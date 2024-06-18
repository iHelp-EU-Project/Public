import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UnsupervisedComponent } from './unsupervised.component';

describe('UnsupervisedComponent', () => {
  let component: UnsupervisedComponent;
  let fixture: ComponentFixture<UnsupervisedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UnsupervisedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UnsupervisedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
