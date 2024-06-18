import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportValuesPerIDColumnsKmeansAREndComponent } from './import-values-per-idcolumns-kmeans-ar-end.component';

describe('ImportValuesPerIDColumnsKmeansAREndComponent', () => {
  let component: ImportValuesPerIDColumnsKmeansAREndComponent;
  let fixture: ComponentFixture<ImportValuesPerIDColumnsKmeansAREndComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportValuesPerIDColumnsKmeansAREndComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportValuesPerIDColumnsKmeansAREndComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
