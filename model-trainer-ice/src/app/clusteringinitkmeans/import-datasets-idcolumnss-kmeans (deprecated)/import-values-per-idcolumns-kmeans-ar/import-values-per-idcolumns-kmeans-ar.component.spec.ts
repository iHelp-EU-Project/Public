import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportValuesPerIDColumnsKmeansARComponent } from './import-values-per-idcolumns-kmeans-ar.component';

describe('ImportValuesPerIDColumnsKmeansARComponent', () => {
  let component: ImportValuesPerIDColumnsKmeansARComponent;
  let fixture: ComponentFixture<ImportValuesPerIDColumnsKmeansARComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportValuesPerIDColumnsKmeansARComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportValuesPerIDColumnsKmeansARComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
