import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadfilesidComponent } from './uploadfilesid.component';

describe('UploadfilesidComponent', () => {
  let component: UploadfilesidComponent;
  let fixture: ComponentFixture<UploadfilesidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadfilesidComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadfilesidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
