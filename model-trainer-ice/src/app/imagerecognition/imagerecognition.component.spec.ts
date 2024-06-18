import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImagerecognitionComponent } from './imagerecognition.component';

describe('ImagerecognitionComponent', () => {
  let component: ImagerecognitionComponent;
  let fixture: ComponentFixture<ImagerecognitionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImagerecognitionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImagerecognitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
