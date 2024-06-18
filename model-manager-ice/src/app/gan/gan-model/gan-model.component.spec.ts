import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GanModelComponent } from './gan-model.component';

describe('GanModelComponent', () => {
  let component: GanModelComponent;
  let fixture: ComponentFixture<GanModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GanModelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GanModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
