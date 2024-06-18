import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusteringbirchComponent } from './clusteringbirch.component';

describe('ClusteringbirchComponent', () => {
  let component: ClusteringbirchComponent;
  let fixture: ComponentFixture<ClusteringbirchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusteringbirchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusteringbirchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
