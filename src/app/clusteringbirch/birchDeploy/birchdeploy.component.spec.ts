import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BirchdeployComponent } from './birchdeploy.component';

describe('BirchdeployComponent', () => {
  let component: BirchdeployComponent;
  let fixture: ComponentFixture<BirchdeployComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BirchdeployComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BirchdeployComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
