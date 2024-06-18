import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeuronalnetworkingcolumnsidAnomaliesComponent } from './neuronalnetworkingcolumnsid-anomalies.component';

describe('NeuronalnetworkingcolumnsidAnomaliesComponent', () => {
  let component: NeuronalnetworkingcolumnsidAnomaliesComponent;
  let fixture: ComponentFixture<NeuronalnetworkingcolumnsidAnomaliesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NeuronalnetworkingcolumnsidAnomaliesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NeuronalnetworkingcolumnsidAnomaliesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
