import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeuronalnetworkcolumsCorreComponent } from './neuronalnetworkcolums-corre.component';

describe('NeuronalnetworkcolumsCorreComponent', () => {
  let component: NeuronalnetworkcolumsCorreComponent;
  let fixture: ComponentFixture<NeuronalnetworkcolumsCorreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NeuronalnetworkcolumsCorreComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NeuronalnetworkcolumsCorreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
