import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeuronalnetworkidComponent } from './neuronalnetworkid.component';

describe('NeuronalnetworkidComponent', () => {
  let component: NeuronalnetworkidComponent;
  let fixture: ComponentFixture<NeuronalnetworkidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NeuronalnetworkidComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NeuronalnetworkidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
