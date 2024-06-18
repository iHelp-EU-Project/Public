import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InitGLMComponent } from './init-glm.component';

describe('InitGLMComponent', () => {
  let component: InitGLMComponent;
  let fixture: ComponentFixture<InitGLMComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InitGLMComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InitGLMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
