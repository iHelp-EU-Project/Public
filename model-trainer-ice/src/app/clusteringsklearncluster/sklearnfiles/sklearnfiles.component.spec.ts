import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SklearnfilesComponent } from './sklearnfiles.component';

describe('SklearnfilesComponent', () => {
  let component: SklearnfilesComponent;
  let fixture: ComponentFixture<SklearnfilesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SklearnfilesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SklearnfilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
