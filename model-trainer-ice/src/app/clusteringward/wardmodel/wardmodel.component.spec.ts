import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WardmodelComponent } from './wardmodel.component';

describe('WardmodelComponent', () => {
  let component: WardmodelComponent;
  let fixture: ComponentFixture<WardmodelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WardmodelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WardmodelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
