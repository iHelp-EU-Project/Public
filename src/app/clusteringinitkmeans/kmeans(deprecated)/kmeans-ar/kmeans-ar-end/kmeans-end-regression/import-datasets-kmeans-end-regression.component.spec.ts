import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportDatasetsKmeansEndRegressionComponent } from './import-datasets-kmeans-end-regression.component';

describe('ImportDatasetsKmeansEndRegressionComponent', () => {
  let component: ImportDatasetsKmeansEndRegressionComponent;
  let fixture: ComponentFixture<ImportDatasetsKmeansEndRegressionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportDatasetsKmeansEndRegressionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportDatasetsKmeansEndRegressionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
