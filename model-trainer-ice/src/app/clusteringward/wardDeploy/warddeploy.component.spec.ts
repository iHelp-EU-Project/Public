import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WarddeployComponent } from './warddeploy.component';

describe('WarddeployComponent', () => {
  let component: WarddeployComponent;
  let fixture: ComponentFixture<WarddeployComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WarddeployComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WarddeployComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
