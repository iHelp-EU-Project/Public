import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeuronalnetworkingcolumnsidCorreComponent } from './neuronalnetworkingcolumnsid-corre.component';

describe('NeuronalnetworkingcolumnsidCorreComponent', () => {
  let component: NeuronalnetworkingcolumnsidCorreComponent;
  let fixture: ComponentFixture<NeuronalnetworkingcolumnsidCorreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NeuronalnetworkingcolumnsidCorreComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NeuronalnetworkingcolumnsidCorreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
