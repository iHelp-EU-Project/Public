import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadmodelComponent } from './readmodel.component';

describe('ReadmodelComponent', () => {
  let component: ReadmodelComponent;
  let fixture: ComponentFixture<ReadmodelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReadmodelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReadmodelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
