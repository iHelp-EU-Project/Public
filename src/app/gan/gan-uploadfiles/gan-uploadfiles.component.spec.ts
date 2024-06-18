import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GanUploadfilesComponent } from './gan-uploadfiles.component';

describe('GanUploadfilesComponent', () => {
  let component: GanUploadfilesComponent;
  let fixture: ComponentFixture<GanUploadfilesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GanUploadfilesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GanUploadfilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
