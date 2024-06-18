import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportDatasetsIDColumnssKmeansComponent } from './import-datasets-idcolumnss-kmeans.component';

describe('ImportDatasetsIDColumnssKmeansComponent', () => {
  let component: ImportDatasetsIDColumnssKmeansComponent;
  let fixture: ComponentFixture<ImportDatasetsIDColumnssKmeansComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImportDatasetsIDColumnssKmeansComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportDatasetsIDColumnssKmeansComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
