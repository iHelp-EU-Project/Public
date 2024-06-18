import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadfilesGLMComponent } from './uploadfiles-glm.component';

describe('UploadfilesGLMComponent', () => {
  let component: UploadfilesGLMComponent;
  let fixture: ComponentFixture<UploadfilesGLMComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadfilesGLMComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadfilesGLMComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
