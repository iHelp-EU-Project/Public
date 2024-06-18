import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BirchmodelComponent } from './birchmodel.component';

describe('BirchmodelComponent', () => {
  let component: BirchmodelComponent;
  let fixture: ComponentFixture<BirchmodelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BirchmodelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BirchmodelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
