import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportDatasetsKmeansEndComponent } from './import-datasets-kmeans-end.component';

describe('ImportDatasetsKmeansEndComponent', () => {
  let component: ImportDatasetsKmeansEndComponent;
  let fixture: ComponentFixture<ImportDatasetsKmeansEndComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportDatasetsKmeansEndComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportDatasetsKmeansEndComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
