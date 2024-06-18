import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CnnComponent } from './cnn.component';

describe('CnnComponent', () => {
  let component: CnnComponent;
  let fixture: ComponentFixture<CnnComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CnnComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CnnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
