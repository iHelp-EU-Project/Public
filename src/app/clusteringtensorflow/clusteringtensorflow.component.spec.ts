import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClusteringtensorflowComponent } from './clusteringtensorflow.component';

describe('ClusteringtensorflowComponent', () => {
  let component: ClusteringtensorflowComponent;
  let fixture: ComponentFixture<ClusteringtensorflowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClusteringtensorflowComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClusteringtensorflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
