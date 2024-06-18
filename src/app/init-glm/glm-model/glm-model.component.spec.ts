import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlmModelComponent } from './glm-model.component';

describe('GlmModelComponent', () => {
  let component: GlmModelComponent;
  let fixture: ComponentFixture<GlmModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GlmModelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GlmModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
