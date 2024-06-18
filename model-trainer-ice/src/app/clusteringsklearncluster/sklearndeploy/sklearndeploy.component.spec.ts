import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SklearndeployComponent } from './sklearndeploy.component';

describe('SklearndeployComponent', () => {
  let component: SklearndeployComponent;
  let fixture: ComponentFixture<SklearndeployComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SklearndeployComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SklearndeployComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
