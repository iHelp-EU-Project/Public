import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CnnModelComponent } from './cnn-model.component';

describe('CnnModelComponent', () => {
  let component: CnnModelComponent;
  let fixture: ComponentFixture<CnnModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CnnModelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CnnModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
