import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReadmodelidComponent } from './readmodelid.component';

describe('ReadmodelidComponent', () => {
  let component: ReadmodelidComponent;
  let fixture: ComponentFixture<ReadmodelidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReadmodelidComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReadmodelidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
