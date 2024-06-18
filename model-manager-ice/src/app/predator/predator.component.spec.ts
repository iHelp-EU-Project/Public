import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PredatorComponent } from './predator.component';

describe('PredatorComponent', () => {
  let component: PredatorComponent;
  let fixture: ComponentFixture<PredatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PredatorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PredatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
