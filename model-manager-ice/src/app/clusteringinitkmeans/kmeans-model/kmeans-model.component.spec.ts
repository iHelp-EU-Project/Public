import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KmeansModelComponent } from './kmeans-model.component';

describe('KmeansModelComponent', () => {
  let component: KmeansModelComponent;
  let fixture: ComponentFixture<KmeansModelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ KmeansModelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(KmeansModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
