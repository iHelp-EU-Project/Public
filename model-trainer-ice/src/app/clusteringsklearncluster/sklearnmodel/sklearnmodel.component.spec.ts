import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SklearnmodelComponent } from './sklearnmodel.component';

describe('SklearnmodelComponent', () => {
  let component: SklearnmodelComponent;
  let fixture: ComponentFixture<SklearnmodelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SklearnmodelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SklearnmodelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
