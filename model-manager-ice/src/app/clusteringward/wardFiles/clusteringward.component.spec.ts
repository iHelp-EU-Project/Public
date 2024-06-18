import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusteringwardComponent } from './clusteringward.component';

describe('ClusteringwardComponent', () => {
  let component: ClusteringwardComponent;
  let fixture: ComponentFixture<ClusteringwardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusteringwardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusteringwardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
