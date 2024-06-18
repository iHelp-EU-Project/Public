import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupervisedComponent } from './supervised.component';

describe('SupervisedComponent', () => {
  let component: SupervisedComponent;
  let fixture: ComponentFixture<SupervisedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SupervisedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SupervisedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
